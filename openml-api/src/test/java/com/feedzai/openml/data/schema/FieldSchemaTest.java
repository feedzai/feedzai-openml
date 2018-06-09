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

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

/**
 * Tests for the {@link FieldSchema}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class FieldSchemaTest {

    /**
     * Tests that an invalid field name cannot be used.
     */
    @Test
    public void testInvalidFieldName() {
        assertThatThrownBy(() -> new FieldSchema("", 3, new NumericValueSchema(false)))
                .as("Error thrown by an invalid field schema construction")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new FieldSchema(null, 3, new NumericValueSchema(false)))
                .as("Error thrown by an invalid field schema construction")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that an invalid field index cannot be used.
     */
    @Test
    public void testInvalidFieldIndex() {
        assertThatThrownBy(() -> new FieldSchema("field1", -4, new NumericValueSchema(false)))
                .as("Error thrown by an invalid field schema construction")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that an invalid field schema cannot be used.
     */
    @Test
    public void testInvalidFieldSchema() {
        assertThatThrownBy(() -> new FieldSchema("field1", 0, null))
                .as("Error thrown by an invalid field schema construction")
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests whether a field schema correctly retrieves its properties.
     */
    @Test
    public void testFieldSchema() {
        final String fieldName = "field1";
        final int fieldIndex = 3;
        final AbstractValueSchema fieldValueSchema = new StringValueSchema(false);
        final FieldSchema field = new FieldSchema(fieldName, fieldIndex, fieldValueSchema);

        assertThat(field.getFieldName())
                .isEqualTo(fieldName);

        assertThat(field.getFieldIndex())
                .isEqualTo(fieldIndex);

        assertThat(field.getValueSchema())
                .isEqualTo(fieldValueSchema);

        assertThat(field)
                .isEqualTo(field)
                .isNotEqualTo(null)
                .isNotEqualTo(new FieldSchema("field2", fieldIndex, fieldValueSchema))
                .isNotEqualTo(new FieldSchema(fieldName, 2, fieldValueSchema))
                .isNotEqualTo(new FieldSchema(fieldName, fieldIndex, new NumericValueSchema(true)));

        assertThat(field.toString())
                .contains(fieldName)
                .contains(String.valueOf(fieldIndex))
                .contains(fieldValueSchema.toString());

        assertThat(field.hashCode())
                .isEqualTo(field.hashCode())
                .isNotEqualTo(new FieldSchema("field2", fieldIndex, fieldValueSchema));
    }

}
