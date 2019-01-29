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

package com.feedzai.openml.data.schema;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for a {@link DatasetSchema}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchemaTest {

    /**
     * A field to use in the tests.
     */
    private static final FieldSchema FIELD_SCHEMA =
            new FieldSchema("field", 0, new NumericValueSchema(false));

    /**
     * Tests that the target index must be correct.
     */
    @Test
    public void testInvalidTargetIndex() {
        assertThatThrownBy(() -> new DatasetSchema(2, ImmutableList.of(FIELD_SCHEMA)))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    /**
     * Tests that by creating a {@link DatasetSchema} with no information on the target variable will make {@link DatasetSchema#getTargetIndex()}
     * return {@link Optional#empty()}.
     */
    @Test
    public final void testNoTargetIndex() {
        final SoftAssertions assertions = new SoftAssertions();
        final DatasetSchema schema = new DatasetSchema(ImmutableList.of(FIELD_SCHEMA));
        assertions.assertThat(schema.getTargetIndex())
                .as("A dataset with no target variable")
                .isNotPresent();

        assertions.assertThat(schema.getTargetVariableField())
                .as("A dataset with no target variable should not return any field.")
                .isNotPresent();
    }

    /**
     * Tests that the fields of the schema can never be null.
     */
    @Test
    public void testNonNullFields() {
        assertThatThrownBy(() -> new DatasetSchema(0, null))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests that the properties of a valid schema can be retrieved.
     */
    @Test
    public void testValidSchema() {
        final FieldSchema targetField =
                new FieldSchema("field3", 3, new CategoricalValueSchema(false, ImmutableSet.of("true", "false")));

        final List<FieldSchema> predictiveFields = ImmutableList.of(
                new FieldSchema("field0", 0, new NumericValueSchema(false)),
                new FieldSchema("field1", 1, new StringValueSchema(true)),
                new FieldSchema("field2", 2, new NumericValueSchema(true))
        );

        final List<FieldSchema> allFields = ImmutableList.<FieldSchema>builder()
                .addAll(predictiveFields)
                .add(targetField)
                .build();

        final DatasetSchema datasetSchema = new DatasetSchema(3, allFields);

        assertThat(datasetSchema.getTargetIndex())
                .hasValue(3);

        assertThat(datasetSchema.getFieldSchemas())
                .isEqualTo(allFields);

        assertThat(datasetSchema.getTargetFieldSchema())
                .isEqualTo(targetField);

        assertThat(datasetSchema.getTargetVariableField())
                .contains(targetField);

        assertThat(datasetSchema.getPredictiveFields())
                .isEqualTo(predictiveFields);

        final DatasetSchema anotherSchema = new DatasetSchema(2, predictiveFields);
        assertThat(datasetSchema)
                .isEqualTo(datasetSchema)
                .isNotEqualTo(null)
                .isNotEqualTo(anotherSchema);

        assertThat(datasetSchema.hashCode())
                .isEqualTo(datasetSchema.hashCode())
                .isNotEqualTo(anotherSchema.hashCode());
    }


    /**
     * Tests that all the {@link FieldSchema} of the {@link DatasetSchema} have unique names.
     */
    @Test
    public void testUniqueFieldName() {
        final FieldSchema targetField =
                new FieldSchema("field3", 3, new CategoricalValueSchema(false, ImmutableSet.of("true", "false")));

        final List<FieldSchema> predictiveFields = ImmutableList.of(
                new FieldSchema("field0", 0, new NumericValueSchema(false)),
                new FieldSchema("field1", 1, new StringValueSchema(true)),
                new FieldSchema("field1", 2, new NumericValueSchema(true))
        );

        final List<FieldSchema> allFields = ImmutableList.<FieldSchema>builder()
                .addAll(predictiveFields)
                .add(targetField)
                .build();

        assertThatThrownBy(() -> new DatasetSchema(targetField.getFieldIndex(), allFields))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that all the {@link FieldSchema} of the {@link DatasetSchema} have unique indexes.
     */
    @Test
    public void testUniqueIndexes() {
        final FieldSchema targetField =
                new FieldSchema("field4", 3, new CategoricalValueSchema(false, ImmutableSet.of("true", "false")));

        final List<FieldSchema> predictiveFields = ImmutableList.of(
                new FieldSchema("field0", 0, new NumericValueSchema(false)),
                new FieldSchema("field1", 1, new StringValueSchema(true)),
                new FieldSchema("field2", 2, new NumericValueSchema(true)),
                new FieldSchema("field3", 2, new NumericValueSchema(true))
        );

        final List<FieldSchema> allFields = ImmutableList.<FieldSchema>builder()
                .addAll(predictiveFields)
                .add(targetField)
                .build();

        assertThatThrownBy(() -> new DatasetSchema(targetField.getFieldIndex(), allFields))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that the {@link FieldSchema} of the {@link DatasetSchema} are sorted by their indexes.
     */
    @Test
    public void testSortedIndexes() {
        final FieldSchema targetField =
                new FieldSchema("field3", 3, new CategoricalValueSchema(false, ImmutableSet.of("true", "false")));

        final List<FieldSchema> predictiveFields = ImmutableList.of(
                new FieldSchema("field0", 0, new NumericValueSchema(false)),
                new FieldSchema("field2", 2, new NumericValueSchema(true)),
                new FieldSchema("field1", 1, new StringValueSchema(true))
        );

        final List<FieldSchema> allFields = ImmutableList.<FieldSchema>builder()
                .addAll(predictiveFields)
                .add(targetField)
                .build();

        assertThatThrownBy(() -> new DatasetSchema(targetField.getFieldIndex(), allFields))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that the {@link FieldSchema} of the {@link DatasetSchema} don't have a missing intermediary index.
     */
    @Test
    public void testMissingIntermedIndex() {
        final FieldSchema targetField =
                new FieldSchema("field4", 4, new CategoricalValueSchema(false, ImmutableSet.of("true", "false")));

        final List<FieldSchema> predictiveFields = ImmutableList.of(
                new FieldSchema("field0", 0, new NumericValueSchema(false)),
                new FieldSchema("field1", 1, new StringValueSchema(true)),
                new FieldSchema("field3", 3, new NumericValueSchema(true))
        );

        final List<FieldSchema> allFields = ImmutableList.<FieldSchema>builder()
                .addAll(predictiveFields)
                .add(targetField)
                .build();

        assertThatThrownBy(() -> new DatasetSchema(targetField.getFieldIndex(), allFields))
                .as("The error thrown by an incorrect construction of a schema")
                .isInstanceOf(IllegalArgumentException.class);    }
}
