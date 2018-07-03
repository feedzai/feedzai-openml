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

package com.feedzai.openml.util.data.encoding;

import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import com.feedzai.openml.util.data.encoding.EncodingHelper;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests to validate the conversions performed by {@link EncodingHelper}.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public class EncodingHelperTest {

    /**
     * Tests the encoding of different types of schema fields.
     */
    @Test
    public void testEncoding() {
        final int numericals = 1;
        final int categoricals = 1;
        final int strings = 1;
        final int totalFields = numericals + categoricals + strings;
        final ImmutableSet<String> nominalValues = ImmutableSet.of("cat1", "cat2");

        final DatasetSchema schema = TestDatasetSchemaBuilder.builder()
                .withNumericalFields(numericals)
                .withCategoricalFields(categoricals, nominalValues)
                .withStringFields(strings)
                .build();

        final EncodingHelper encodingHelper = new EncodingHelper(schema);
        assertThat(encodingHelper.numberFields())
                .as("The number of encoders")
                .isEqualTo(totalFields);

        final List<FieldSchema> fieldSchemas = schema.getFieldSchemas();
        for (int fieldIndex = 0; fieldIndex < totalFields; fieldIndex++) {
            final FieldSchema fieldSchema = fieldSchemas.get(fieldIndex);
            if (fieldSchema.getValueSchema() instanceof NumericValueSchema) {
                validateNumericFieldEncoding(encodingHelper, fieldIndex);

            } else if (fieldSchema.getValueSchema() instanceof CategoricalValueSchema) {
                validateCategoricalFieldEncoding(encodingHelper, fieldIndex, nominalValues);

            } else if (fieldSchema.getValueSchema() instanceof StringValueSchema) {
                validateStringFieldEncoding(encodingHelper, fieldIndex);

            } else {
                throw new RuntimeException("Unexpected type of field schema");
            }
        }

        assertThatThrownBy(() -> encodingHelper.encode(1.0, totalFields + 1))
                .as("Encoding with an index too large")
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> encodingHelper.encode(1.0, -1))
                .as("Encoding with a negative index")
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    /**
     * Tests that the {@link EncodingHelper#classToIndexConverter(CategoricalValueSchema)} correctly translates values
     * and returns null when an unexpected value is converted.
     */
    @Test
    public void testClassToIndex() {
        final CategoricalValueSchema schema = new CategoricalValueSchema(
                true,
                ImmutableSet.of(
                        "fraud",
                        "genuine"
                )
        );
        final Function<Serializable, Integer> converter = EncodingHelper.classToIndexConverter(schema);

        assertThat(converter.apply("fraud"))
                .as("Converting a valid value")
                .isEqualTo(0);

        assertThat(converter.apply("ERROR"))
                .as("Converting an unexpected value")
                .isNull();
    }

    /**
     * Tests that the method that reverts an encoding of a category index back to the category value returns the correct
     * value or throws {@link IndexOutOfBoundsException} for invalid indexes.
     */
    @Test
    public void testDecodeCategorical() {

        final CategoricalValueSchema schema = new CategoricalValueSchema(
                true,
                ImmutableSet.of(
                        "fraud",
                        "genuine"
                )
        );

        assertThat(EncodingHelper.decodeDoubleToCategory(1.0, schema))
                .as("Converting a valid index")
                .isEqualTo("genuine");

        assertThatThrownBy(() -> EncodingHelper.decodeDoubleToCategory(2.0, schema))
                .as("Converting an index too large")
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> EncodingHelper.decodeDoubleToCategory(-3.0, schema))
                .as("Converting a negative index")
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    /**
     * Validates that a {@link EncodingHelper.SerializableEncoder} for a
     * {@link StringValueSchema} field handles correct an error values as expected.
     *
     * @param encodingHelper The {@link EncodingHelper} to test.
     * @param fieldIndex     The index of the field.
     */
    private void validateStringFieldEncoding(final EncodingHelper encodingHelper, final int fieldIndex) {
        assertThat(encodingHelper.encode(null, fieldIndex))
                .as("Encoding null")
                .isNull();

        assertThat(encodingHelper.encode("VALUE", fieldIndex))
                .as("Encoding a value")
                .isEqualTo("VALUE");
    }

    /**
     * Validates that a {@link EncodingHelper.SerializableEncoder} for a
     * {@link CategoricalValueSchema} field handles correct an error values as expected.
     *
     * @param encodingHelper The {@link EncodingHelper} to test.
     * @param fieldIndex     The index of the field.
     * @param nominalValues  The nominal values that were defined in the {@link CategoricalValueSchema} field.
     */
    private void validateCategoricalFieldEncoding(final EncodingHelper encodingHelper,
                                                  final int fieldIndex,
                                                  final ImmutableSet<String> nominalValues) {
        final ImmutableList<String> nominalValuesList = ImmutableList.copyOf(nominalValues);
        for (int valueIndex = 0; valueIndex < nominalValuesList.size(); valueIndex++) {
            assertThat(encodingHelper.encode(nominalValuesList.get(valueIndex), fieldIndex))
                    .as("Encoding a valid value")
                    .isEqualTo((double) valueIndex);
        }

        assertThat(encodingHelper.encode(null, fieldIndex))
                .as("Encoding null")
                .isEqualTo(Double.NaN);

        assertThat(encodingHelper.encode("ERROR", fieldIndex))
                .as("Encoding invalid category")
                .isEqualTo(Double.NaN);
    }

    /**
     * Validates that a {@link EncodingHelper.SerializableEncoder} for a
     * {@link NumericValueSchema} field handles correct an error values as expected.
     *
     * @param encodingHelper The {@link EncodingHelper} to test.
     * @param fieldIndex     The index of the field.
     */
    private void validateNumericFieldEncoding(final EncodingHelper encodingHelper, final int fieldIndex) {
        assertThat(encodingHelper.encode(3, fieldIndex))
                .as("Encoding a positive integer")
                .isEqualTo(3.0);

        assertThat(encodingHelper.encode(3.3, fieldIndex))
                .as("Encoding a positive double")
                .isEqualTo(3.3);

        assertThat(encodingHelper.encode(-3, fieldIndex))
                .as("Encoding a negative integer")
                .isEqualTo(-3.0);

        assertThat(encodingHelper.encode(-3.3, fieldIndex))
                .as("Encoding a negative double")
                .isEqualTo(-3.3);

        assertThat(encodingHelper.encode(Double.NaN, fieldIndex))
                .as("Encoding NaN")
                .isEqualTo(Double.NaN);

        assertThat(encodingHelper.encode(null, fieldIndex))
                .as("Encoding null")
                .isEqualTo(Double.NaN);

        assertThat(encodingHelper.encode("ERROR", fieldIndex))
                .as("Encoding a string")
                .isEqualTo(Double.NaN);
    }
}
