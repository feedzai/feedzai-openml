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

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.model.MachineLearningModel;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link ClassificationValidationUtils}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ClassificationValidationUtilsTest {

    /**
     * The schema for the data to classify.
     */
    private static final DatasetSchema SCHEMA = TestDatasetSchemaBuilder.builder()
            .withCategoricalFields(2)
            .withNumericalFields(3)
            .withCategoricalTarget()
            .build();

    /**
     * A valid path of a file.
     *
     * @since 0.3.0
     */
    private static Path VALID_PATH;

    /**
     * A valid {@link Map} of parameters to valid that a model can be loaded.
     *
     * @since 0.3.0
     */
    private static Map<String, String> VALID_PARAM_MAP = ImmutableMap.of("k1", "v2");

    /**
     * A invalid {@link Map} of parameters to valid that a model cannot be loaded.
     *
     * @since 0.3.0
     */
    private static Map<String, String> INVALID_PARAM_MAP = ImmutableMap.of();

    /**
     * Message of the error that will be thrown during the validation of a model to load.
     *
     * @since 0.3.0
     */
    private static final String INVALID_MODEL_TO_LOAD_MSG = "map is empty";

    /**
     * Part of the error message when a validation method is called with a null parameter.
     *
     * @since 0.3.0
     */
    private static final String NULL_ERROR_MSG = "cannot be null";

    @BeforeClass
    public static void beforeClass() throws IOException {
        VALID_PATH = Files.createTempFile("temp", ".out");
        VALID_PATH.toFile().deleteOnExit();
    }

    /**
     * Tests that a valid classification model is correctly assessed as such.
     */
    @Test
    public void testValidModel() {
         assertThatCode(() -> ClassificationValidationUtils.validateClassificationModel(
                SCHEMA,
                getClassificationModelFor(
                        () -> new double[] { 0.25, 0.75 },
                        () -> 1
                )
        )).doesNotThrowAnyException();
    }

    /**
     * Tests that a classification model that cannot yield a distribution is correctly detected.
     */
    @Test
    public void testModelInvalidDistribution() {
        assertThatThrownBy(() -> ClassificationValidationUtils.validateClassificationModel(
                SCHEMA,
                getClassificationModelFor(
                        () -> {
                            throw new RuntimeException("Failure to calculate distribution");
                        },
                        () -> 1
                )
        )).isInstanceOf(ModelLoadingException.class);
    }

    /**
     * Tests that a classification model that cannot classify is correctly detected.
     */
    @Test
    public void testModelInvalidClassification() {
        assertThatThrownBy(() -> ClassificationValidationUtils.validateClassificationModel(
                SCHEMA,
                getClassificationModelFor(
                        () -> new double[] { 0.25, 0.75 },
                        () -> {
                            throw new RuntimeException("Failure to calculate distribution");
                        }
                )
        )).isInstanceOf(ModelLoadingException.class);
    }

    /**
     * Tests that a model to load is successfully validated when the parameters are valid.
     *
     * @since 0.3.0
     */
    @Test
    public void testValidParamsModelToLoad() {
        assertThatCode(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        getMachineLearningModelLoader(),
                        VALID_PATH,
                        SCHEMA,
                        VALID_PARAM_MAP
                ))
                .doesNotThrowAnyException();
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the parameters of a model to load are not valid.
     *
     * @since 0.3.0
     */
    @Test
    public void testInvalidParamsModelToLoad() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        getMachineLearningModelLoader(),
                        VALID_PATH,
                        SCHEMA,
                        INVALID_PARAM_MAP
                ))
                .isInstanceOf(ModelLoadingException.class)
                .hasMessage(INVALID_MODEL_TO_LOAD_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the {@link DatasetSchema} of a classification model
     * used to score events is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidSchemaOfClassificationModel() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateClassificationModel(
                        null,
                        getClassificationModelFor(
                                () -> new double[] { 0.25, 0.75 },
                                () -> 1
                        )
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the {@link ClassificationMLModel} used to score events
     * is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidClassificationMLModel() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateClassificationModel(
                        SCHEMA,
                        null
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the {@link MachineLearningModelLoader} of a model to
     * load is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidModelLoaderOfModelToLoad() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        null,
                        VALID_PATH,
                        SCHEMA,
                        VALID_PARAM_MAP
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the {@link Path} of a model to load is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidPathOfModelToLoad() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        getMachineLearningModelLoader(),
                        null,
                        SCHEMA,
                        VALID_PARAM_MAP
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the {@link DatasetSchema} of a model to load is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidSchemaOfModelToLoad() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        getMachineLearningModelLoader(),
                        VALID_PATH,
                        null,
                        VALID_PARAM_MAP
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Tests that a {@link ModelLoadingException} is thrown when the map of parameters of a model to load is null.
     *
     * @since 0.3.0
     */
    @Test
    public void testVoidParamOfModelToLoad() {
        assertThatThrownBy(
                () -> ClassificationValidationUtils.validateParamsModelToLoad(
                        getMachineLearningModelLoader(),
                        VALID_PATH,
                        SCHEMA,
                        null
                ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(NULL_ERROR_MSG);
    }

    /**
     * Creates a classification model that always yields the given distribution of classes and predicted class.
     *
     * @param distributionSupplier The supplier for the distribution of classes.
     * @param classSupplier        The supplier for the class to predict.
     * @return The classification model.
     */
    private ClassificationMLModel getClassificationModelFor(final Supplier<double[]> distributionSupplier,
                                                            final Supplier<Integer> classSupplier) {

        return new ClassificationMLModel() {
            @Override
            public double[] getClassDistribution(final Instance instance) {
                return distributionSupplier.get();
            }

            @Override
            public int classify(final Instance instance) {
                return classSupplier.get();
            }

            @Override
            public boolean save(final Path dir, final String name) {
                throw new UnsupportedOperationException("Not supposed to be called.");
            }

            @Override
            public DatasetSchema getSchema() {
                throw new UnsupportedOperationException("Not supposed to be called.");
            }

            @Override
            public void close() throws Exception {
                // Empty on purpose.
            }
        };
    }

    /**
     * Creates a model loader used to validate the parameters of a model to load. The validation will fail if the map
     * of parameters is empty.
     *
     * @since 0.3.0
     */
    private MachineLearningModelLoader getMachineLearningModelLoader() {

        return new MachineLearningModelLoader() {
            @Override
            public MachineLearningModel loadModel(final Path modelPath,
                                                  final DatasetSchema schema) {
                return null;
            }

            @Override
            public DatasetSchema loadSchema(final Path modelPath) {
                return null;
            }

            @Override
            public List<ParamValidationError> validateForLoad(final Path modelPath,
                                                              final DatasetSchema schema,
                                                              final Map params) {
                if (params.isEmpty()) {
                    return ImmutableList.of(new ParamValidationError(INVALID_MODEL_TO_LOAD_MSG));
                }
                return ImmutableList.of();
            }
        };
    }

}
