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

package com.feedzai.openml.util.validate;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.mocks.MockDataset;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
     * @param schema Schema of the loaded model.
     * @param model  Loaded model.
     * @throws ModelLoadingException Exception thrown when the model has validation problems.
     */
    public static void validateClassificationModel(final DatasetSchema schema,
                                                   final ClassificationMLModel model) throws ModelLoadingException {
        Preconditions.checkNotNull(schema, "schema cannot be null");
        Preconditions.checkNotNull(model, "model cannot be null");

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

    /**
     * Validates that the model to load can be used with the given parameters.
     *
     * @param modelLoader An instance responsible for instantiating models.
     * @param modelPath   Path of the model.
     * @param schema      Schema of the model.
     * @param params      Collection of parameters and the corresponding values.
     * @throws ModelLoadingException If the path and/or schema are not valid.
     * @since 0.2.3
     */
    public static void validateParamsModelToLoad(final MachineLearningModelLoader modelLoader,
                                                 final Path modelPath,
                                                 final DatasetSchema schema,
                                                 final Map<String, String> params) throws ModelLoadingException {
        Preconditions.checkNotNull(modelLoader, "modelLoader cannot be null");
        Preconditions.checkNotNull(modelPath, "modelPath cannot be null");
        Preconditions.checkNotNull(schema, "schema cannot be null");
        Preconditions.checkNotNull(params, "params cannot be null");

        logger.debug("Validating the parameters of the model in path [{}]", modelPath);
        final List<ParamValidationError> validationErrors = modelLoader.validateForLoad(
                modelPath,
                schema,
                params
        );
        if (!validationErrors.isEmpty()) {
            final String errorMsg = validationErrors.stream()
                    .map(ParamValidationError::getMessage)
                    .collect(Collectors.joining(","));
            throw new ModelLoadingException(errorMsg);
        }
        logger.debug("Parameters of the model in path [{}] successfully validated", modelPath);
    }
}
