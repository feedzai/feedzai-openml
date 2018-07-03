/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 */

package com.feedzai.openml.util.data;

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.ClassificationMLModel;

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
     * @return The number of classes on the target variable.
     */
    public static int getNumClassValues(final DatasetSchema datasetSchema) {

        final AbstractValueSchema valueSchema = datasetSchema
                .getFieldSchemas()
                .get(datasetSchema.getTargetIndex())
                .getValueSchema();

        return getNumClassValues(valueSchema);
    }

    /**
     * Gets the number of classes in the given schema's target variable assuming that it is for a classification
     * problem.
     *
     * @param targetValueSchema The {@link AbstractValueSchema} of the target variable.
     * @return The number of classes on the target variable.
     */
    public static int getNumClassValues(final AbstractValueSchema targetValueSchema) {
        if (targetValueSchema instanceof CategoricalValueSchema) {
            return ((CategoricalValueSchema) targetValueSchema).getNominalValues().size();
        } else {
            throw new RuntimeException("The target variable is not a categorical value: " + targetValueSchema);
        }
    }
}
