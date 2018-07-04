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

import com.feedzai.openml.data.Dataset;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.exception.ModelTrainingException;
import com.feedzai.openml.provider.model.MachineLearningModelTrainer;
import com.feedzai.openml.util.algorithm.MLAlgorithmEnum;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

/**
 * Contains tests for model training with a Provider.
 *
 * @param <M> The type of a class that extends {@link ClassificationMLModel}.
 * @param <L> The type of a class that extends {@link MachineLearningModelTrainer}.
 * @param <P> The type of a class that extends {@link MachineLearningProvider}.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
public abstract class AbstractProviderModelTrainTest<M extends ClassificationMLModel,
                                                     L extends MachineLearningModelTrainer<M>,
                                                     P extends MachineLearningProvider<L>>
        extends AbstractProviderModelBaseTest<M, L, P> {

    /**
     * Tries to train the Train Dataset ({@link #getTrainDataset()}) with each trainable algorithm
     * of the provider ({@link #getTrainAlgorithms()}).
     *
     * @throws ModelTrainingException If there is an error with the training.
     */
    @Test
    public void trainModelsForAllAlgorithms() throws ModelTrainingException {

        for (final Map.Entry<MLAlgorithmEnum, Map<String, String>> algorithm : getTrainAlgorithms().entrySet()) {

            final Dataset dataset = getTrainDataset();
            final L modelTrainer = getMachineLearningModelLoader(algorithm.getKey());

            final M model = modelTrainer.fit(dataset, new Random(0), algorithm.getValue());

            model.classify(getDummyInstance());
            model.getClassDistribution(getDummyInstance());
        }
    }

    /**
     * Dataset used to train models in tests for this provider.
     *
     * @return A Dataset to be used in test training.
     */
    protected abstract Dataset getTrainDataset();

    /**
     * List of Algorithms that support training in this provider.
     *
     * @return The list of trainable algorithms in this provider.
     */
    protected abstract Map<MLAlgorithmEnum, Map<String, String>> getTrainAlgorithms();
}
