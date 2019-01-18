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

package com.feedzai.openml.util.data;

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.model.ClassificationMLModel;

import java.util.Optional;
import java.util.function.Function;

/**
 * Utilities for manipulating a {@link DatasetSchema} of a {@link ClassificationMLModel classification ML problem}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public final class ClassificationDatasetSchemaUtil {

    /**
     * Private constructor for utility class.
     */
    private ClassificationDatasetSchemaUtil() { }

    /**
     * Gets the number of classes in the given schema's target variable assuming that it is for a classification
     * problem.
     *
     * @param datasetSchema The {@link DatasetSchema}.
     * @return The number of classes on the target variable, if a target variable is defined, or {@link Optional#empty()} otheriwse.
     */
    public static Optional<Integer> getNumClassValues(final DatasetSchema datasetSchema) {
        return datasetSchema.getTargetIndex()
                .map(datasetSchema.getFieldSchemas()::get)
                .map(FieldSchema::getValueSchema)
                .map(ClassificationDatasetSchemaUtil::getNumClassValues);
    }

    /**
     * Gets the number of classes in the given schema's target variable assuming that it is for a classification
     * problem.
     *
     * @param targetValueSchema The {@link AbstractValueSchema} of the target variable.
     * @return The number of classes on the target variable.
     */
    public static int getNumClassValues(final AbstractValueSchema targetValueSchema) {
        return withCategoricalValueSchema(
                targetValueSchema,
                categoricalValueSchema -> categoricalValueSchema.getNominalValues().size()
        ).orElseThrow(() -> new RuntimeException("The target variable is not a categorical value: " + targetValueSchema));
    }

    /**
     * Template method to execute custom functions on the {@link CategoricalValueSchema}.
     * This method checks wether the given {@link AbstractValueSchema} is a {@link CategoricalValueSchema}, and
     * executes the given {@link Function block} if true, otherwise returns an empty {@link Optional}.
     *
     * @param targetValueSchema The {@link AbstractValueSchema} of the target variable.
     * @param block The function to execute if the given target variable schema is {@link CategoricalValueSchema categorical}.
     * @param <T> The return type of the given {@code block}.
     * @return An {@link Optional} with the return value of the given {@link Function block} if the
     * received {@link AbstractValueSchema} is a {@link CategoricalValueSchema},
     * otherwise it will return an empty {@link Optional}.
     */
    public static <T> Optional<T> withCategoricalValueSchema(final AbstractValueSchema targetValueSchema, final Function<CategoricalValueSchema, T> block) {
        if (targetValueSchema instanceof CategoricalValueSchema) {
            return Optional.of(block.apply(((CategoricalValueSchema) targetValueSchema)));
        } else {
            return Optional.empty();
        }
    }
}
