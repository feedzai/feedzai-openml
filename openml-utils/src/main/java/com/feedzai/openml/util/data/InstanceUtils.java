/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 */

package com.feedzai.openml.util.data;

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
