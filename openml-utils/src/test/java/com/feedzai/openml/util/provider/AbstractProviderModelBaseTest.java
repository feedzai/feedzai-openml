/*
 * Copyright 2018 Feedzai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.feedzai.openml.util.provider;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.exception.ModelTrainingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.feedzai.openml.util.algorithm.MLAlgorithmEnum;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Contains the base tests to be run for every Provider.
 *
 * @param <M> The type of a class that extends {@link ClassificationMLModel}.
 * @param <L> The type of a class that extends {@link MachineLearningModelLoader}.
 * @param <P> The type of a class that extends {@link MachineLearningProvider}.
 * @author Luis Reis (luis.reis@feedzai.com)
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public abstract class AbstractProviderModelBaseTest<M extends ClassificationMLModel,
                                                    L extends MachineLearningModelLoader<M>,
                                                    P extends MachineLearningProvider<L>> {

    /**
     * Number of threads used by tests.
     */
    private final int maxNumberOfThreads = 8;

    /**
     * The maximum timeout (in seconds) to wait for {@link CountDownLatch}.
     *
     * @since 0.2.0
     */
    private final int latchTimeout = 3;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                       TESTS                       *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Checks that is possible to get a {@link MachineLearningProvider} given a valid provider and algorithm.
     */
    @Test
    public void validMachineLearningModelLoader() {
        final P machineLearningProvider = getMachineLearningProvider();

        assertThat(machineLearningProvider.getName())
                .as("name of the provider")
                .isNotEmpty()
                .isNotBlank();

        assertThat(machineLearningProvider.getAlgorithms())
                .as("algorithms supported by the provider")
                .isNotEmpty();

        assertThat(machineLearningProvider.getModelCreator(getValidAlgorithm().getName()).isPresent())
                .as("the MachineLearningModelLoader")
                .isTrue();
    }

    /**
     * Checks that isn't possible to get a {@link MachineLearningProvider} given an invalid algorithm.
     */
    @Test
    public void inValidMachineLearningModelLoader() {
        final P machineLearningProvider = getMachineLearningProvider();
        assertThat(machineLearningProvider.getModelCreator("not_an_algorithm").isPresent())
                .as("the MachineLearningModelLoader")
                .isFalse();
    }

    /**
     * Checks that is possible to create (load/train) two different models at the same time by the main thread.
     * Then It checks that is possible to classify dummy instances.
     *
     * @throws ModelLoadingException If anything goes wrong during loading.
     * @throws ModelTrainingException If anything goes wrong during training.
     */
    @Test
    public void createTwoModelsInOneThreadTest() throws ModelLoadingException, ModelTrainingException {
        final M firstModel = getFirstModel();
        final M secondModel = getSecondModel();

        final Instance instance = getDummyInstance();

        final Set targetValuesOfFirstModel = getClassifyValuesOfFirstModel();

        assertThat(firstModel.classify(instance))
                .as("There is an error with the prediction value")
                .isIn(targetValuesOfFirstModel);

        final double[] fistClassDistribution = firstModel.getClassDistribution(instance);
        assertThat(fistClassDistribution)
                .as("There is an error with the class distribution")
                .hasSize(targetValuesOfFirstModel.size());

        assertThat(Arrays.stream(fistClassDistribution).sum())
                .as("sum of the class distribution")
                .isCloseTo(1.0, within(0.01));

        assertThat(secondModel.getClassDistribution(instance))
                .as("models should be different")
                .hasSize(targetValuesOfFirstModel.size())
                .isNotEqualTo(fistClassDistribution);
    }

    /**
     * Evaluates one model created (loaded/trained) in the main thread in multiple threads concurrently.
     * The model will evaluate two different instances, that will be injected at the same frequency (half of
     * {@link #maxNumberOfThreads}). Afterwards, the results of the class distributions are asserted to verify that
     * there are only two distinct results with the same frequency (half of {@link #maxNumberOfThreads}).
     *
     * @throws ModelLoadingException If anything goes wrong during loading.
     * @throws ModelTrainingException If anything goes wrong during training.
     */
    @Test
    public void createOneModelAndEvaluateInMultipleThreadsTest() throws ModelLoadingException, ModelTrainingException {
        final ExecutorService executor = Executors.newFixedThreadPool(this.maxNumberOfThreads);
        final M firstModel = getFirstModel();

        final List<Future<double[]>> evaluationsList = classDistTwoInstancesInParallel(firstModel, executor);

        // Wait for all classifications to finish
        final List<double[]> classDistributionResults = getUnorderedClassificationResults(evaluationsList);

        final Map<List<Double>, Long> groupedClassDistResults = classDistributionResults
                .stream()
                .map(arr -> Arrays.asList(ArrayUtils.toObject(arr)))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        groupedClassDistResults.forEach((key, count) -> assertThat(count)
                .as(String.format("The number of times the classification %s should appear", key))
                .isEqualTo(this.maxNumberOfThreads / 2));
    }

    /**
     * Evaluates two models created (loaded/trained) in the main thread in multiple threads concurrently.
     *
     * @throws ModelLoadingException If anything goes wrong during loading.
     * @throws ModelTrainingException If anything goes wrong during training.
     */
    @Test
    public void createTwoModelsInMultipleThreadsTest() throws ModelLoadingException, ModelTrainingException {
        final ExecutorService executor = Executors.newFixedThreadPool(this.maxNumberOfThreads * 2);
        final M firstModel = getFirstModel();
        final M secondModel = getSecondModel();

        final List<List<Future<Integer>>> evaluationsList = classifyInstancesInParallel(
                ImmutableList.of(firstModel, secondModel),
                executor
        );
        assertAllInstances(evaluationsList);
    }

    /**
     * Creates (loads/trains) two models in multiple threads concurrently and then evaluate them in multiple new threads.
     */
    @Test
    public void createModelsInThreadsAndEvaluateInOtherThreadsTest() {
        final ExecutorService executorGet = Executors.newFixedThreadPool(this.maxNumberOfThreads);
        final ExecutorService executorClassify = Executors.newFixedThreadPool(this.maxNumberOfThreads);

        final List<Callable<M>> callableGetModels = ImmutableList.of(this::getFirstModel, this::getSecondModel);
        final List<Future<M>> futureModelList = callableGetModels.stream()
                .map(executorGet::submit)
                .collect(Collectors.toList());
        final List<M> modelList = futureModelList.parallelStream().map(
                future -> {
                    try {
                        return future.get();
                    } catch (final InterruptedException | ExecutionException e) {
                        throw new RuntimeException("Error while waiting for parallel evaluations.", e);
                    }
                }
        ).collect(Collectors.toList());

        final List<List<Future<Integer>>> evaluationsList = classifyInstancesInParallel(modelList, executorClassify);

        assertAllInstances(evaluationsList);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                        UTIL                       *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Asserts that all instances were classified.
     *
     * @param evaluationsList A list of futures that will return the class of each instance.
     */
    private void assertAllInstances(final List<List<Future<Integer>>> evaluationsList) {
        // Wait for all classifications to finish
        final List<List<Integer>> classifications = evaluationsList.stream()
                .map(this::getUnorderedClassificationResults)
                .collect(Collectors.toList());

        // Assert all instances were classified (these may be out of order)
        classifications.forEach(
                classification ->
                        assertThat(classification)
                                .as("models should be different")
                                .isSubsetOf(
                                        ImmutableList.<Integer>builder()
                                                .addAll(getClassifyValuesOfFirstModel())
                                                .addAll(getClassifyValuesOfSecondModel())
                                                .build()
                                )
        );
    }

    /**
     * Classifies dummy instances using a given model in parallel using a given {@link ExecutorService}.
     *
     * @param model    Model to use to classify dummy instances.
     * @param executor Executor to use to launch the threads that will classify each instance.
     * @return A list of futures that will return the class of each instance.
     */
    private List<Future<Integer>> classifyInstancesInParallel(final M model,
                                                              final ExecutorService executor) {
        final CountDownLatch latch = new CountDownLatch(1);

        final List<Future<Integer>> results = IntStream.range(0, this.maxNumberOfThreads)
                .mapToObj(idx -> executor.submit(() -> {
                    if (!latch.await(latchTimeout, TimeUnit.SECONDS)) {
                        throw new RuntimeException("Timeout awaiting for tests to run in parallel");
                    }
                    return model.classify(getDummyInstance());
                }))
                .collect(Collectors.toList());

        latch.countDown();
        return results;
    }

    /**
     * Classifies one of two dummy instances using a given model in parallel using a given {@link ExecutorService}.
     * This method creates the same number of events for each dummy instance.
     *
     * @param model    Model to use to classify dummy instances.
     * @param executor Executor to use to launch the threads that will classify each instance.
     * @return A list of futures that will return the class distribution of each instance.
     * @since 0.2.0
     */
    private List<Future<double[]>> classDistTwoInstancesInParallel(final M model,
                                                                   final ExecutorService executor) {
        final CountDownLatch latch = new CountDownLatch(1);

        final List<Future<double[]>> results = IntStream.range(0, this.maxNumberOfThreads)
                .mapToObj(idx -> executor.submit(() -> {
                    if (!latch.await(latchTimeout, TimeUnit.SECONDS)) {
                        throw new RuntimeException("Timeout awaiting for tests to run in parallel");
                    }
                    final Instance instance = idx % 2 == 0 ? getDummyInstance() : getDummyInstanceDifferentResult();
                    return model.getClassDistribution(instance);
                }))
                .collect(Collectors.toList());

        latch.countDown();
        return results;
    }

    /**
     * Classifies dummy instances using a given a list of models in parallel using a given {@link ExecutorService}.
     *
     * @param modelList A list of models to use to classify dummy instances.
     * @param executor  Executor to use to launch the threads that will classify each instance.
     * @return A list of futures that will return the class of each instance.
     */
    private List<List<Future<Integer>>> classifyInstancesInParallel(final List<M> modelList,
                                                                    final ExecutorService executor) {
        return modelList.stream()
                .map(model -> classifyInstancesInParallel(model, executor))
                .collect(Collectors.toList());
    }

    /**
     * Waits for the results of the parallel classification done with {@link #classifyInstancesInParallel}.
     *
     * @param evaluations List of futures that was returned by {@link #classifyInstancesInParallel}.
     * @return A list with the classes of the dummy instances.
     */
    private <T> List<T> getUnorderedClassificationResults(final List<Future<T>> evaluations) {
        return evaluations.parallelStream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                fail("Error while waiting for parallel evaluations.");
                return null;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Gets the {@link MachineLearningModelLoader} for a given algorithm.
     *
     * @param algorithm The algorithm used in the model loader.
     * @return the {@link MachineLearningModelLoader}.
     */
    protected L getMachineLearningModelLoader(final MLAlgorithmEnum algorithm) {
        final P modelProvider = getMachineLearningProvider();
        final Optional<L> modelCreator =
                modelProvider.getModelCreator(algorithm.getName());
        return modelCreator.get();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                  ABSTRACT METHODS                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Gets the first {@link ClassificationMLModel} to be used in test.
     *
     * @return a instance of {@link ClassificationMLModel}.
     * @throws ModelLoadingException  If anything goes wrong during loading.
     * @throws ModelTrainingException If anything goes wrong during training.
     */
    public abstract M getFirstModel() throws ModelLoadingException, ModelTrainingException;

    /**
     * Gets the second {@link ClassificationMLModel} to be used in test.
     *
     * @return a instance of {@link ClassificationMLModel}.
     * @throws ModelLoadingException If anything goes wrong during loading.
     * @throws ModelTrainingException If anything goes wrong during training.
     */
    public abstract M getSecondModel() throws ModelLoadingException, ModelTrainingException;

    /**
     * Gets a set with the possible values for an {@link Instance} when classified by {@link #getFirstModel}.
     *
     * @return a set with the index of the class nominal value.
     */
    public abstract Set<Integer> getClassifyValuesOfFirstModel();

    /**
     * Gets a set with the possible values for an {@link Instance} when classified by {@link #getSecondModel()}.
     *
     * @return a set with the index of the class nominal value.
     */
    public abstract Set<Integer> getClassifyValuesOfSecondModel();

    /**
     * Gets the {@link MachineLearningModelLoader} used by {@link #getFirstModel}.
     *
     * @return the {@link MachineLearningModelLoader} used by {@link #getFirstModel}.
     */
    public abstract L getFirstMachineLearningModelLoader();

    /**
     * Gets an instance of {@link MachineLearningProvider}.
     *
     * @return the {@link MachineLearningProvider}.
     */
    public abstract P getMachineLearningProvider();

    /**
     * Gets a dummy {@link Instance} to be classified by a {@link ClassificationMLModel}.
     *
     * @return a dummy {@link Instance}.
     */
    public abstract Instance getDummyInstance();

    /**
     * Gets a dummy {@link Instance} to be classified by a {@link ClassificationMLModel} that returns different results
     * from the results produced by {@link #getDummyInstance()};
     *
     * @return a dummy {@link Instance}.
     * @since 0.2.0
     */
    public abstract Instance getDummyInstanceDifferentResult();

    /**
     * Create the an instance of the {@link DatasetSchema} used in tests.
     *
     * @param targetValues The nominal values of the target field.
     * @return an instance of {@link DatasetSchema}.
     */
    public abstract DatasetSchema createDatasetSchema(final Set<String> targetValues);

    /**
     * Gets a string with a name of an algorithm.
     *
     * @return a name of an algorithm.
     */
    public abstract MLAlgorithmEnum getValidAlgorithm();

    /**
     * Gets a set with the values of the target variable of {@link #getFirstModel}.
     *
     * @return the values of the target variable.
     */
    public abstract Set<String> getFirstModelTargetNominalValues();
}
