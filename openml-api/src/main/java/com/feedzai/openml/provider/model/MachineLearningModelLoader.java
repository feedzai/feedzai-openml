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
import com.feedzai.openml.provider.exception.ModelLoadingException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * An entity responsible for instantiating a specific {@link MachineLearningModel}.
 *
 * @param <T> The {@link MachineLearningModel} this entity is responsible for instantiating.
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface MachineLearningModelLoader<T extends MachineLearningModel> {

    /**
     * Loads the {@link MachineLearningModel} that has been serialized into the provided path.
     *
     * @param modelPath The {@link Path} to the location where the model has been persisted.
     * @param schema    The {@link DatasetSchema} the model uses.
     * @return The {@link MachineLearningModel} associated with this entity, ready to be used.
     * @throws ModelLoadingException If a problem occurred while loading the model.
     */
    T loadModel(Path modelPath, DatasetSchema schema) throws ModelLoadingException;

    /**
     * Validates that the algorithm can be used to load a model with the given {@link DatasetSchema} for the provided
     * parameters.
     *
     * @param modelPath The {@link Path} to the location where the model has been persisted.
     * @param schema    The {@link DatasetSchema schema of the dataset} to be feeded into the algorithm.
     * @param params    The collection of parameters and the corresponding values.
     * @return A possible empty list of {@link ParamValidationError}s. An empty list means no problems/errors were
     * found, and therefore the algorithm can be used for a {@link Dataset} with the corresponding {@link DatasetSchema}
     * and parameters.
     */
    List<ParamValidationError> validateForLoad(Path modelPath, DatasetSchema schema, Map<String, String> params);

    /**
     * Loads the {@link DatasetSchema} with the data schema used to train a {@link MachineLearningModel}.
     * <p>
     * This method assumes that the provided path contains a json (model.json) with the representation of the data
     * schema. It will return an empty optional if the json file doesn't exist.
     *
     * @param modelPath The path where the algorithm has been serialized.
     * @return a optional with the {@link DatasetSchema} used to train a {@link MachineLearningModel}.
     * @throws ModelLoadingException If a problem occurred while loading the schema.
     */
    DatasetSchema loadSchema(Path modelPath) throws ModelLoadingException;
}
