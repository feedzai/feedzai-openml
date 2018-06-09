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

package com.feedzai.openml.data;


import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;

/**
 * An instance is a container of values for each defined feature.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface Instance {

    /**
     * Gets the value this instance holds for the feature on the given index (according to
     * an associated {@link DatasetSchema}), as a double. This index is zero based.
     * <p>
     * Possible types of values implement {@link AbstractValueSchema} and with the exception
     * of {@link StringValueSchema} they should be encoded as a double for compactness.
     * <p>
     * If a feature has type {@link CategoricalValueSchema}, then the returned number
     * represents the index of the category in the {@link CategoricalValueSchema#getNominalValues()}, where the index
     * is 0-based.
     * <p>
     * If a feature has type {@link StringValueSchema} this method should throw a runtime
     * exception when attempting to get the value of that feature.
     *
     * @param index The index of the field.
     * @return The double representation of the field in case the feature is a {@link CategoricalValueSchema} or
     * {@link NumericValueSchema}.
     * In the former an index to the nominal values of the categorical field is returned as a double.
     * In case of a {@link StringValueSchema} an exception should be thrown.
     */
    double getValue(int index);

    /**
     * Gets a String values this instance holds for the feature on the given index.
     * <p>
     * If a feature has type other than {@link StringValueSchema} this method should throw a runtime
     * exception when attempting to get the value of that feature.
     *
     * @param index The index of the field.
     * @return The String representation of the field in case the feature is a {@link StringValueSchema}. An exception should
     * be thrown otherwise.
     */
    String getStringValue(final int index);
}
