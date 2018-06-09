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

package com.feedzai.openml.example;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.mocks.MockInstance;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link ExampleMLProvider}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ExampleMLProviderTest {

    /**
     * Tests the example provider.
     *
     * @throws ModelLoadingException If the model cannot be loaded, which would be unexpected.
     */
    @Test
    public void test() throws ModelLoadingException {

        final DatasetSchema datasetSchema = new DatasetSchema(0, ImmutableList.of(
                new FieldSchema("field1", 0, new CategoricalValueSchema(false, ImmutableSet.of("fraud", "notfraud"))),
                new FieldSchema("field2", 1, new NumericValueSchema(false)),
                new FieldSchema("field3", 2, new NumericValueSchema(false))
        ));

        final MachineLearningProvider<MachineLearningModelLoader<? extends ClassificationMLModel>> provider =
                new ExampleMLProvider();

        assertThat(provider.getAlgorithms())
                .as("The description of the algorithms provided")
                .hasSize(2);

        testProviderFor(datasetSchema, provider, 0, ExampleMLProvider.PREDICT_FIRST);
        testProviderFor(datasetSchema, provider, 1, ExampleMLProvider.PREDICT_SECOND);
    }

    /**
     * Tests the provider for the given model configuration.
     *
     * @param mockedSchema  A mocked schema.
     * @param provider      The provider to test.
     * @param expectedClass The class that is expected to always be predicted.
     * @param algName       The name of the algorithm to use.
     * @throws ModelLoadingException If the model cannot be loaded, which would be unexpected.
     */
    private void testProviderFor(final DatasetSchema mockedSchema,
                                 final MachineLearningProvider<MachineLearningModelLoader<? extends ClassificationMLModel>> provider,
                                 final int expectedClass,
                                 final String algName) throws ModelLoadingException {

        final Optional<MachineLearningModelLoader<? extends ClassificationMLModel>> modelCreatorOpt =
                provider.getModelCreator(algName);
        assertThat(modelCreatorOpt)
                .as("The optional creator")
                .isPresent();

        final MachineLearningModelLoader<? extends ClassificationMLModel> modelLoader = modelCreatorOpt.get();

        final Path modelPath = Paths.get("does not matter");
        assertThat(modelLoader.validateForLoad(modelPath, mockedSchema, ImmutableMap.of()))
                .as("The validation errors found")
                .isEmpty();

        final ClassificationMLModel model = modelLoader.loadModel(modelPath, mockedSchema);

        final Instance instance = new MockInstance(3, ThreadLocalRandom.current());
        assertThat(model.classify(instance))
                .as("The predicted class")
                .isEqualTo(expectedClass);

        final double[] classDistribution = model.getClassDistribution(instance);
        assertThat(DoubleStream.of(classDistribution).sum())
                .as("scores for instance")
                .isCloseTo(1.0, Offset.offset(1.0e-9));
        assertThat(classDistribution[expectedClass])
                .as("The expected class probability")
                .isCloseTo(1.0, Offset.offset(1.0e-9));
    }

}
