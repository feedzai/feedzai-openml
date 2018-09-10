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

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.MachineLearningModel;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.util.load.LoadModelUtils;
import com.feedzai.openml.util.load.LoadSchemaUtils;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Supplier;

/**
 * Class containing common utility methods to validate the loading and training of {@link MachineLearningModel}.
 *
 * @since 0.1.0
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 */
public final class ValidationUtils {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    /**
     * Empty constructor for utility class.
     */
    private ValidationUtils() { }

    /**
     * Validates that all the required params (as described by the given {@link MLAlgorithmDescriptor}) are present,
     * and that all parameters are valid.
     *
     * @param algorithmDescriptor The {@link MLAlgorithmDescriptor} for the algorithm.
     * @param params The parameters to be validated.
     * @return A List of {@link ParamValidationError}, possibly empty if no errors were found.
     */
    public static List<ParamValidationError> checkParams(final MLAlgorithmDescriptor algorithmDescriptor, final Map<String, String> params) {
        final ImmutableList.Builder<ParamValidationError> errorsBuilder = ImmutableList.builder();

        algorithmDescriptor.getParameters().forEach(parameterConfig -> {
            final String parameterName = parameterConfig.getName();
            final String value = params.get(parameterName);

            checkMandatoryParam(parameterConfig, value).ifPresent(errorsBuilder::add);
            checkParam(parameterConfig, value).ifPresent(errorsBuilder::add);
        });

        return errorsBuilder.build();
    }

    /**
     * Validates that a mandatory parameter is present.
     *
     * @param parameter The {@link ModelParameter parameter descriptor}.
     * @param parameterValue The parameter value to validate.
     * @return An {@link Optional} {@link ParamValidationError}, that it's empty if no error was found, and non-empty otherwise.
     */
    private static Optional<ParamValidationError> checkMandatoryParam(final ModelParameter parameter,
                                                                      final String parameterValue) {
        if (null == parameterValue && parameter.isMandatory()) {
            return Optional.of(new ParamValidationError(
                    parameter.getName(),
                    parameterValue,
                    String.format("The parameter %s must have a value since it's mandatory", parameter.getName())
            ));
        }

        return Optional.empty();
    }

    /**
     * Validates that a parameter is valid as enforced by it's {@link ModelParameter configuration}.
     *
     * @param parameter The {@link ModelParameter parameter descriptor}.
     * @param parameterValue The parameter value to validate.
     * @return An {@link Optional} {@link ParamValidationError}, that it's empty if no error was found, and non-empty otherwise.
     */
    private static Optional<ParamValidationError> checkParam(final ModelParameter parameter,
                                                                         final String parameterValue) {
        if (parameterValue != null) {
            return parameter.getFieldType().validate(parameter.getName(), parameterValue);
        }

        return Optional.empty();
    }

    /**
     * Validates that a given schema represents a dataset whose target variable is {@link CategoricalValueSchema}
     * with at least 2 values.
     *
     * @param schema The schema for the model.
     * @return An {@link Optional} {@link ParamValidationError}.
     */
    public static Optional<ParamValidationError> validateCategoricalSchema(final DatasetSchema schema) {

        final AbstractValueSchema targetSchemaType = schema.getTargetFieldSchema().getValueSchema();

        final Supplier<SortedSet<String>> categoricalNominalValues = () -> ((CategoricalValueSchema) targetSchemaType).getNominalValues();

        if (!(targetSchemaType instanceof CategoricalValueSchema)) {
            return Optional.of(new ParamValidationError(String.format(
                    "Target variable %s must be a categorical field",
                    schema.getTargetFieldSchema().getFieldName()
            )));

        } else if (categoricalNominalValues.get().size() < 2) {
            final String message = String.format(
                    "Target variable %s must be a categorical field with at least 2 values, but only has %s.",
                    schema.getTargetFieldSchema().getFieldName(),
                    categoricalNominalValues.get().size()
            );
            return Optional.of(new ParamValidationError(message));

        } else {
            return Optional.empty();
        }
    }

    /**
     * Performs a validation that there is a model binary within the given path according to {@link LoadModelUtils}.
     *
     * @param modelPath The path to the model directory of a provider.
     * @return A possible list of errors.
     */
    public static List<ParamValidationError> validateModelInDir(final Path modelPath) {
        try {
            LoadModelUtils.getModelFilePath(modelPath);
        } catch (final ModelLoadingException e) {
            final String msg = String.format("Could not load model from path %s: %s", modelPath, e.getMessage());
            logger.warn(msg);
            return ImmutableList.of(new ParamValidationError(msg));
        }

        return Collections.emptyList();
    }

    /**
     * Perform the common validations to be done during the load of a {@link MachineLearningModel}.
     *
     * @param schema The {@link DatasetSchema schema of the dataset} to be fed into the algorithm.
     * @param params The collection of parameters and the corresponding values.
     * @return A list of {@link ParamValidationError} with the problems/error found during the validation.
     */
    public static List<ParamValidationError> baseLoadValidations(final DatasetSchema schema, final Map<String, String> params) {

        final ImmutableList.Builder<ParamValidationError> errorBuilder = ImmutableList.builder();

        if (Objects.isNull(schema)) {
            errorBuilder.add(new ParamValidationError("DatasetSchema cannot be null"));
        }

        if (Objects.isNull(params)) {
            errorBuilder.add(new ParamValidationError("The map of parameters cannot be null"));
        }

        return errorBuilder.build();
    }

    /**
     * Validates that a schema contains no fields of a given type.
     *
     * @param schema     The {@link DatasetSchema schema of the dataset} to be feeded into the algorithm.
     * @param fieldClass The class of the field value schema that is not allowed.
     * @return A list of {@link ParamValidationError} with the problems/error found during the validation.
     */
    public static List<ParamValidationError> checkNoFieldsOfType(final DatasetSchema schema,
                                                                 final Class<? extends AbstractValueSchema> fieldClass) {
        final ImmutableList.Builder<ParamValidationError> errors = ImmutableList.builder();

        schema.getPredictiveFields().stream()
                .filter(field -> fieldClass.isInstance(field.getValueSchema()))
                .forEach(field -> errors.add(
                        new ParamValidationError(String.format(
                                "field [%s] is of unsupported type [%s]",
                                field.getFieldName(),
                                LoadSchemaUtils.getValueSchemaTypeToString(field.getValueSchema())
                        ))
                ));

        return errors.build();
    }

    /**
     * Validates that the given path is usable for persisting a trained model. It verifies that the folder already
     * exists, that it is readable and has no contents inside.
     *
     * @param pathToPersist The path where the model is to be persisted.
     * @return A possible list of errors, if any.
     */
    public static List<ParamValidationError> validateModelPathToTrain(final Path pathToPersist) {
        try {
            if (pathToPersist.toFile().exists() && Files.list(pathToPersist).count() != 0) {
                final String msg = String.format(
                        "The selected folder %s to persist the Machine Learning Model (yet to be trained) must be empty but is currently not.",
                        pathToPersist
                );
                return Collections.singletonList(new ParamValidationError(msg));
            }
        } catch (final Exception e) {
            logger.warn("Unexpected error while verifying indicated directory {} to persist ML model (to be trained).", pathToPersist);
            final String msg = String.format(
                    "Could not access the selected folder %s to persist the Machine Learning Model (yet to be trained) due to error %s.",
                    pathToPersist,
                    e.getMessage()
            );
            return Collections.singletonList(new ParamValidationError(msg));
        }
        return Collections.emptyList();
    }
}
