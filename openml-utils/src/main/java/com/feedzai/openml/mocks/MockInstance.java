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

package com.feedzai.openml.mocks;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import com.feedzai.util.data.ClassificationDatasetSchemaUtil;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Mock class implementation for an instance.
 * Used for tests and validation of models.
 * <p>
 * By default creates an instance with {@code fieldSize} floating point values ranging [0,10[.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
public class MockInstance implements Instance, Serializable {

    /**
     * Range for mock values in numeric fields.
     */
    private static final double
            MAX_NUMERIC_MOCK_VALUE = 10,
            MIN_NUMERIC_MOCK_VALUE = -10;

    /**
     * Number of random bytes to use when generating mock String values.
     */
    private static final int NUM_MOCK_STRING_RANDOM_BYTES = 32;

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -6261681440631914349L;

    /**
     * Values of the instance.
     */
    private final List<Serializable> values;

    /**
     * Constructor for the instance based on a given schema.
     *
     * @param schema Schema to which this instance will conform.
     * @param random Random number generator used to generate the instances.
     */
    public MockInstance(final DatasetSchema schema, final Random random) {
        this.values = schema.getFieldSchemas().stream()
                .map(FieldSchema::getValueSchema)
                .map(generateRandomFieldValue(random))
                .collect(Collectors.toList());
    }

    /**
     * Constructor for an instance with only numeric fields.
     *
     * @param numberFieldsSize Number of fields (numeric only) the instance will have.
     * @param random           Random number generator used to generate the instances.
     */
    public MockInstance(final int numberFieldsSize, final Random random) {
        this.values = IntStream.range(0, numberFieldsSize)
                .mapToObj(index -> new NumericValueSchema(false))
                .map(generateRandomFieldValue(random))
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.values);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MockInstance other = (MockInstance) obj;
        return Objects.equal(this.values, other.values);
    }

    /**
     * Returns a function that generates random values for a field of the instance given the field schema.
     *
     * @implNote This method returns a function for better integration with Stream::map.
     * @param random Random number
     * @return A function that given a field schema returns a correct random value for that schema.
     */
    private Function<AbstractValueSchema, Serializable> generateRandomFieldValue(final Random random) {
        return (AbstractValueSchema valueSchema) -> {
            if (valueSchema instanceof NumericValueSchema) {
                // Numeric random value: [MIN_NUMERIC_MOCK_VALUE, MAX_NUMERIC_MOCK_VALUE[
                return MIN_NUMERIC_MOCK_VALUE + (MAX_NUMERIC_MOCK_VALUE - MIN_NUMERIC_MOCK_VALUE) * random.nextDouble();

            } else if (valueSchema instanceof StringValueSchema) {
                // String random value: NUM_MOCK_STRING_RANDOM_BYTES Base64 URL encoded bytes
                final byte[] randomBytes = new byte[NUM_MOCK_STRING_RANDOM_BYTES]; random.nextBytes(randomBytes);
                final byte[] base64encodedBytes = Base64.getUrlEncoder().encode(randomBytes);
                return new String(base64encodedBytes);

            } else {
                // Categorical random value: Index in [0, <Size of Nominal Values>[
                final int numClassValues = ClassificationDatasetSchemaUtil.getNumClassValues(valueSchema);
                return (double) random.nextInt(numClassValues);
            }
        };
    }

    /**
     * Constructor for the instance.
     *
     * @param values Values of the instance.
     */
    public MockInstance(final List<Serializable> values) {
        this.values = values;
    }

    /**
     * Constructor for the instance.
     *
     * @param values Values of the instance.
     */
    public MockInstance(final double[] values) {
        this.values = Arrays.stream(values).boxed().collect(Collectors.toList());
    }

    @Override
    public double getValue(final int index) {
        return (double) this.values.get(index);
    }

    @Override
    public String getStringValue(final int index) {
        return (String) this.values.get(index);
    }
}
