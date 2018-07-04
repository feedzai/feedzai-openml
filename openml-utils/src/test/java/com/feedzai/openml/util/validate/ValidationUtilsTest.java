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

import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests on the basic validation methods of {@link ValidationUtils}.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public class ValidationUtilsTest {

    /**
     * Tests that the {@link ValidationUtils#baseLoadValidations(DatasetSchema, Map)} accepts a valid
     * {@link DatasetSchema}.
     */
    @Test
    public void testBaseValidationOK() {
        final DatasetSchema schema = TestDatasetSchemaBuilder.builder()
                .withCategoricalFields(3)
                .withNumericalFields(3)
                .withStringFields(3)
                .build();

        assertThat(ValidationUtils.baseLoadValidations(schema, ImmutableMap.of("param1", "val1")))
                .as("The result of validating a valid schema and params map")
                .isEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#checkNoFieldsOfType(DatasetSchema, Class)} does not return errors when
     * searching for categorical fields in a schema without categoricals.
     */
    @Test
    public void testCheckNoFieldsOfTypeOK() {
        final DatasetSchema schemaWithoutCat = TestDatasetSchemaBuilder.builder()
                .withNumericalFields(3)
                .withStringFields(3)
                .build();

        assertThat(ValidationUtils.checkNoFieldsOfType(schemaWithoutCat, CategoricalValueSchema.class))
                .as("Validation of a schema without categoricals")
                .isEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#checkNoFieldsOfType(DatasetSchema, Class)} does returns an error when
     * searching for categorical fields in a schema containing a categorical.
     */
    @Test
    public void testCheckNoFieldsOfTypeCategoricalWithInvalidSchema() {
        final DatasetSchema schemaWithoutCat =  TestDatasetSchemaBuilder.builder()
                .withCategoricalFields(3)
                .withNumericalFields(3)
                .withStringFields(3)
                .build();

        assertThat(ValidationUtils.checkNoFieldsOfType(schemaWithoutCat, CategoricalValueSchema.class))
                .as("Validation of a schema that has a forbidden field type.")
                .isNotEmpty();
    }
}
