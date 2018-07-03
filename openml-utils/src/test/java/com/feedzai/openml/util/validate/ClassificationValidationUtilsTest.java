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
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.util.validate.ClassificationValidationUtils;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import org.junit.Test;

import java.nio.file.Path;
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

}
