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

package com.feedzai.util.data;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.mocks.MockInstance;
import com.feedzai.openml.util.data.InstanceUtils;
import com.feedzai.util.data.schema.TestDatasetSchemaBuilder;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates the behavior of {@link InstanceUtils}.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public class InstanceUtilsTest {

    /**
     * The {@link DatasetSchema} to use in the tests.
     */
    private DatasetSchema schema;

    /**
     * Sets up the schema to use in the test.
     */
    @Before
    public void setUp() {
        this.schema = TestDatasetSchemaBuilder.builder()
                .withNumericalFields(2)
                .withCategoricalFields(2, ImmutableSet.of("false", "true"))
                .build();
    }

    /**
     * Tests that the {@link InstanceUtils#getClassValue(Instance, DatasetSchema)} method actually returns the field
     * value.
     */
    @Test
    public void testGetClassValue() {
        // Make the field values be their index * 10 for easier assertion
        final double[] fieldValues = fieldValues(index -> index * 10.0);
        final MockInstance instance = new MockInstance(fieldValues);

        assertThat(InstanceUtils.getClassValue(instance, this.schema))
                .as("The class value for the instance")
                .isEqualTo(this.schema.getTargetIndex() * 10.0);
    }

    /**
     * Tests that the {@link InstanceUtils#isMissingClass(Instance, DatasetSchema)} method returns the expected value
     * when the target variable is and isn't {@link Double#NaN}, the agreed representation of missing values.
     */
    @Test
    public void testIsMissingClass() {
        final double[] fieldValues = fieldValues(index -> index);
        final MockInstance instanceNotMissing = new MockInstance(fieldValues);

        assertThat(InstanceUtils.isMissingClass(instanceNotMissing, this.schema))
                .as("Check when the class is present")
                .isFalse();

        fieldValues[this.schema.getTargetIndex()] = Double.NaN;
        final MockInstance instanceMissing = new MockInstance(fieldValues);

        assertThat(InstanceUtils.isMissingClass(instanceMissing, this.schema))
                .as("Check when the class is missing")
                .isTrue();
    }

    /**
     * Tests that the {@link InstanceUtils#isMissing(Instance, int)} method returns the expected value when the field is
     * and isn't {@link Double#NaN}, the agreed representation of missing values.
     */
    @Test
    public void testIsMissing() {
        final int nonTargetIndex = (this.schema.getTargetIndex() + 1) % this.schema.getFieldSchemas().size();

        final double[] fieldValues = fieldValues(index -> index);
        final MockInstance instanceNotMissing = new MockInstance(fieldValues);

        assertThat(InstanceUtils.isMissing(instanceNotMissing, nonTargetIndex))
                .as("Check when there is a value present")
                .isFalse();

        fieldValues[nonTargetIndex] = Double.NaN;
        final MockInstance instanceMissing = new MockInstance(fieldValues);

        assertThat(InstanceUtils.isMissing(instanceMissing, nonTargetIndex))
                .as("Check when the value is missing")
                .isTrue();
    }

    /**
     * Generates an array of doubles with the correct size for {@link Instance}s of schema {@link #schema}.
     *
     * @param indexToValue The strategy on how to generate each field's value based on the index.
     * @return An array of doubles.
     */
    private double[] fieldValues(final IntToDoubleFunction indexToValue) {
        return IntStream.range(0, this.schema.getFieldSchemas().size())
                .mapToDouble(indexToValue)
                .toArray();
    }
}
