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

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link CategoricalValueSchema} validation behaviour.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class CategoricalValueSchemaTest {

    /**
     * A value that doesn't belong the the list of nominal values.
     */
    private static final String NON_NOMINAL_VALUE = "random_non_nominal_value";

    /**
     * Test nominal values to use.
     */
    private static final Set<String> NOMINAL_VALUES = ImmutableSet.of("val0", "val1");

    /**
     * Tests getting the nominal values.
     */
    @Test
    public void testNominalValues() {
        assertThat(new CategoricalValueSchema(true, NOMINAL_VALUES).getNominalValues())
                .as("The nominal values")
                .isEqualTo(NOMINAL_VALUES);
    }

    /**
     * Tests the value validation with an instance that allows missing values.
     */
    @Test
    public void testAllowingMissingValues() {
        final CategoricalValueSchema valueSchema = new CategoricalValueSchema(true, NOMINAL_VALUES);

        assertTrue("Missing value should be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
        assertTrue("Validation of a existing nominal value should return true", valueSchema.validate(NOMINAL_VALUES.iterator().next()));
        assertFalse("Non nominal value should not be valid", valueSchema.validate(NON_NOMINAL_VALUE));

        assertThat(valueSchema.isAllowMissing())
                .as("whether it allows missing values")
                .isTrue();
    }

    /**
     * Tests the value validation with an instance that does not allow missing values.
     */
    @Test
    public void testNotAllowingMissingValues() {
        final CategoricalValueSchema valueSchema = new CategoricalValueSchema(false, NOMINAL_VALUES);

        assertFalse("Missing value should not be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
        assertTrue("Validation of a existing nominal value should return true", valueSchema.validate(NOMINAL_VALUES.iterator().next()));
        assertFalse("Non nominal value should not be valid", valueSchema.validate(NON_NOMINAL_VALUE));

        assertThat(valueSchema.isAllowMissing())
                .as("whether it allows missing values")
                .isFalse();
    }

    /**
     * Tests the string representation.
     */
    @Test
    public void testToString() {
        final CategoricalValueSchema valueSchema = new CategoricalValueSchema(false, NOMINAL_VALUES);

        NOMINAL_VALUES.forEach(val ->
                assertThat(valueSchema.toString())
                        .as("The string representation")
                        .contains(val)
        );
    }

    /**
     * Tests the equals implementation.
     */
    @Test
    public void testEquals() {
        final CategoricalValueSchema valueSchema = new CategoricalValueSchema(false, NOMINAL_VALUES);
        final CategoricalValueSchema valueSchema2 = new CategoricalValueSchema(false, NOMINAL_VALUES);
        final CategoricalValueSchema valueSchema3 = new CategoricalValueSchema(true, NOMINAL_VALUES);
        final Set<String> nominalValues2 = ImmutableSet.of("val0", "val1", "val3");
        final CategoricalValueSchema valueSchema4 = new CategoricalValueSchema(false, nominalValues2);

        assertThat(valueSchema)
                .isEqualTo(valueSchema)
                .isEqualTo(valueSchema2)
                .isNotEqualTo(valueSchema3)
                .isNotEqualTo(valueSchema4)
                .isNotEqualTo(null);
    }

    /**
     * Tests the hash code implementation.
     */
    @Test
    public void testHashCode() {
        final CategoricalValueSchema valueSchema = new CategoricalValueSchema(false, NOMINAL_VALUES);
        final CategoricalValueSchema valueSchema2 = new CategoricalValueSchema(false, NOMINAL_VALUES);
        final CategoricalValueSchema valueSchema3 = new CategoricalValueSchema(true, NOMINAL_VALUES);
        final Set<String> nominalValues2 = ImmutableSet.of("val0", "val1", "val3");
        final CategoricalValueSchema valueSchema4 = new CategoricalValueSchema(false, nominalValues2);

        assertThat(valueSchema.hashCode())
                .isEqualTo(valueSchema.hashCode())
                .isEqualTo(valueSchema2.hashCode())
                .isNotEqualTo(valueSchema3.hashCode())
                .isNotEqualTo(valueSchema4.hashCode());
    }
}
