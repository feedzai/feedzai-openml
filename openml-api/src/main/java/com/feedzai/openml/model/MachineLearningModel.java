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

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.MachineLearningProvider;

import java.nio.file.Path;

/**
 * Base interface that every Machine Learning Model should implement.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface MachineLearningModel extends AutoCloseable {

    /**
     * Saves this model into the specified {@link Path}.
     * The format used is responsibility of the {@link MachineLearningModel} implementation. It should be done in such
     * a way that the corresponding {@link MachineLearningProvider} can load the binary
     * representation.
     *
     * @param dir  The path to a directory where to save the model.
     * @param name The name of the model.
     * @return A boolean indicating whether the save was successful or not.
     */
    boolean save(Path dir, String name);

//    /**
//     * Use a bridge design pattern to access explanations if there are any
//     *
//     * @return
//     */
//    default <C extends ExplanationsConfig> Optional<ExplanationsAlgorithm<C>> explanationsAlgorithm() {
//        return Optional.empty();
//    }
//
    /**
     * Gets the {@link DatasetSchema} associated with this Machine Learning Model.
     *
     * @return The Machine Learning Model respective DatasetSchema.
     */
    DatasetSchema getSchema();

}
