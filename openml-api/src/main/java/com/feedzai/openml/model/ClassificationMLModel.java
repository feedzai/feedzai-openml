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

package com.feedzai.openml.model;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;

/**
 * Base interface that every Classification {@link MachineLearningModel} should implement.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface ClassificationMLModel extends MachineLearningModel {

    /**
     * Calculates the class probabilities distribution for an {@link Instance}.
     *
     * @param instance The {@link Instance} to be classified.
     * @return An array of doubles with class probabilities distribution.
     */
    double[] getClassDistribution(Instance instance);

    /**
     * Classifies an {@link Instance} according to the classes provided by the {@link DatasetSchema} feed to the algorithm.
     *
     * @param instance The {@link Instance} to be classified.
     * @return The index of the class nominal value according to the {@link DatasetSchema}
     * provided during training of the model.
     *
     * @deprecated The idea is to classify the biggest value of the class probabilities distribution obtained from #getClassDistribution(),
     *  We no longer need this because we can just obtain the biggest value from the class probabilities distribution itself.
     *
     */
    @Deprecated
    int classify(Instance instance);

}
