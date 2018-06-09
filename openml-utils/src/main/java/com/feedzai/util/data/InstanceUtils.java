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

package com.feedzai.util.data;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.StringValueSchema;

/**
 * Utility class to help computing instance information for instances that do not possess
 * {@link StringValueSchema String} fields.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public final class InstanceUtils {

    /**
     * Private constructor for utility class.
     */
    private InstanceUtils() {
    }

    /**
     * Returns the {@link Instance}'s field value as double for the {@link DatasetSchema#getTargetIndex() target
     * variable} field.
     *
     * @param instance The {@link Instance} to get the class value for.
     * @param schema   The {@link DatasetSchema} of the Instance.
     * @return The target field value as double.
     */
    public static double getClassValue(final Instance instance, final DatasetSchema schema) {
        return instance.getValue(schema.getTargetIndex());
    }

    /**
     * Checks whether the {@link Instance}'s target value is missing.
     *
     * @param instance The Instance to check.
     * @param schema   The {@link DatasetSchema} of the Instance.
     * @return {@code true} whether the Instance is missing information for the target variable, {@code false} if the
     * target variable has a value present.
     */
    public static boolean isMissingClass(final Instance instance, final DatasetSchema schema) {
        return Double.isNaN(getClassValue(instance, schema));
    }

    /**
     * Checks whether a given {@link Instance} field is a missing value.
     *
     * @param instance The Instance to check.
     * @param index    The index of the Instance field to check.
     * @return {@code true} whether the Instance does not information for the field, {@code false} if the field has a
     * value present.
     */
    public static boolean isMissing(final Instance instance, final int index) {
        return Double.isNaN(instance.getValue(index));
    }

}
