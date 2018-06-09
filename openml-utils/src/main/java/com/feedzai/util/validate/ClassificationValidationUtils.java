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

package com.feedzai.util.validate;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.mocks.MockDataset;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Class containing common utility methods to validate classification models.
 *
 * @since 0.1.0
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 */
public final class ClassificationValidationUtils {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ClassificationValidationUtils.class);

    /**
     * Constructor for utility class.
     */
    private ClassificationValidationUtils() { }

    /**
     * Validates that a given classification model is capable of scoring according to the given schema.
     *
     * @param schema      Schema of the loaded model.
     * @param model       Loaded model.
     * @throws ModelLoadingException Exception thrown when the model has validation problems.
     */
    public static void validateClassificationModel(final DatasetSchema schema, final ClassificationMLModel model) throws ModelLoadingException {
        final MockDataset mockDataset = new MockDataset(schema, 1, new Random(0));
        try {
            model.classify(mockDataset.instance(0));
        } catch (final RuntimeException e) {
            final String msg = String.format("Model classification is not compatible with the given schema %s.", schema);

            logger.error(msg, e);
            throw new ModelLoadingException(msg, e);
        }
        try {
            model.getClassDistribution(mockDataset.instance(0));
        } catch (final RuntimeException e) {
            final String msg = "Model does not support class distribution.";

            logger.error(msg, e);
            throw new ModelLoadingException(msg, e);
        }
    }
}
