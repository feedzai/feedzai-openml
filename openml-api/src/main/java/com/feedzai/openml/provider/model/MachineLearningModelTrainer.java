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

package com.feedzai.openml.provider.model;

import com.feedzai.openml.data.Dataset;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.MachineLearningModel;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelTrainingException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * An entity responsible for instantiating a specific {@link MachineLearningModel}. It adds the capability of
 * instantiating a {@link MachineLearningModel} by training it, through the {@link #fit(Dataset, Random, Map)}.
 *
 * @param <T> The {@link MachineLearningModel} this entity is responsible for instantiating.
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface MachineLearningModelTrainer<T extends MachineLearningModel> extends MachineLearningModelLoader<T> {

    /**
     * Fits the {@link MachineLearningModel} to the given {@link Dataset}.
     *
     * @param dataset       The {@link Dataset} containing the data.
     * @param random        A random object to be used as the source of randomness to allow repeatable results.
     * @param params        The collection of parameters and the corresponding values.
     * @return A {@link MachineLearningModel} trained and ready to be used.
     * @throws ModelTrainingException If any problem occurs training the algorithm.
     */
    T fit(Dataset dataset, Random random, Map<String, String> params) throws ModelTrainingException;

    /**
     * Validates that the algorithm can be used to fit a model with the given {@link DatasetSchema} for the provided
     * parameters.
     *
     * @param pathToPersist The {@link Path} to validate for persistence of the model after fitting.
     * @param schema    The {@link DatasetSchema schema of the dataset} to be feeded into the algorithm.
     * @param params    The collection of parameters and the corresponding values.
     * @return A possible empty list of {@link ParamValidationError}s. An empty list means no problems/errors were
     * found, and therefore the algorithm can be used for a {@link Dataset} with the corresponding {@link DatasetSchema}
     * and parameters.
     */
    List<ParamValidationError> validateForFit(Path pathToPersist, DatasetSchema schema, Map<String, String> params);
}
