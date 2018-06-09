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

package com.feedzai.util.data.encoding;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class to deal with encoding and decoding the values of an {@link Instance} into the original representation.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public final class EncodingHelper implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 8695157460825684874L;

    /**
     * Default value to coalesce to when a categorical field value is null.
     */
    private static final Double DEFAULT_CATEGORICAL_VALUE = Double.NaN;

    /**
     * Default value to coalesce to when a numeric field value is null.
     */
    private static final Double DEFAULT_NUMERIC_VALUE = Double.NaN;

    /**
     * Default value to coalesce to when a string field value is null.
     */
    private static final Object DEFAULT_STRING_VALUE = null;

    /**
     * The actual encoders.
     */
    private final List<SerializableEncoder> encoders;

    /**
     * Creates a new {@link EncodingHelper} with prebuilt {@link SerializableEncoder}s for all the fields in the given
     * {@link DatasetSchema}.
     *
     * @param schema The {@link DatasetSchema} of the {@link Instance}s whose fields will need to be encoded.
     */
    public EncodingHelper(final DatasetSchema schema) {
        Preconditions.checkNotNull(schema, "The schema must not be null.");

        final ImmutableList.Builder<SerializableEncoder> converters = ImmutableList.builder();

        for (final FieldSchema fieldSchema : schema.getFieldSchemas()) {
            converters.add(encoderForField(fieldSchema.getValueSchema()));
        }

        this.encoders = converters.build();
    }

    /**
     * Generates a {@link SerializableEncoder} for a given {@link AbstractValueSchema}.
     *
     * @param abstractValueSchema The {@link AbstractValueSchema field schema} to generate an encoder for.
     * @return The encoder.
     */
    public static SerializableEncoder encoderForField(final AbstractValueSchema abstractValueSchema) {

        if (abstractValueSchema instanceof NumericValueSchema) {
            final SerializableEncoder function = value -> Double.valueOf(value.toString());
            return value -> (Double) handleNullOrFailed(value, function, DEFAULT_NUMERIC_VALUE);

        } else if (abstractValueSchema instanceof CategoricalValueSchema) {
            final CategoricalValueSchema asCategorical = (CategoricalValueSchema) abstractValueSchema;

            final Map<String, Double> conversionMap = conversionMap(asCategorical, index -> (double) index);

            final SerializableEncoder function = value -> conversionMap.getOrDefault(value.toString(), DEFAULT_CATEGORICAL_VALUE);
            return value -> (Double) handleNullOrFailed(value, function, DEFAULT_CATEGORICAL_VALUE);

        } else {
            final SerializableEncoder function = Object::toString;
            return value -> (String) handleNullOrFailed(value, function, DEFAULT_STRING_VALUE);
        }
    }

    /**
     * Decodes the encoded (double) representation of a Categorical feature into the original textual representation.
     *
     * @param doubleValue            The encoded value as a double.
     * @param categoricalValueSchema The {@link CategoricalValueSchema} associated with the given feature.
     * @return The original textual representation of the categorical feature.
     * @throws IndexOutOfBoundsException If the encoded value does not correspond to any known category.
     * @see Instance#getValue(int) for more info on the encoding strategy.
     */
    public static String decodeDoubleToCategory(final double doubleValue,
                                                final CategoricalValueSchema categoricalValueSchema) {

        // Each possible category is encoded into a numeric representation, which corresponds to an index. As such we
        // convert the double value into a integer without any information loss
        final int index = (int) doubleValue;

        return Iterables.get(categoricalValueSchema.getNominalValues(), index);
    }

    /**
     * Generates a Function that converts the class value (e.g. "FRAUD") to the index of that value in the
     * {@link CategoricalValueSchema}'s possible values.
     *
     * @param targetVariableSchema The {@link CategoricalValueSchema field schema} to generate an encoder for.
     * @return The conversion function which returns the class index or null if the class is not known.
     */
    public static Function<Serializable, Integer> classToIndexConverter(final CategoricalValueSchema targetVariableSchema) {
        final Map<String, Integer> conversionMap = conversionMap(targetVariableSchema, Function.identity());

        return value -> conversionMap.get(value.toString());
    }

    /**
     * Builds a Map that converts from a category to some value.
     *
     * @param categoricalValueSchema The definition of the categorical field.
     * @param indexToValue           A Function that produces the Map's value given a categorical field index.
     * @param <T>                    The type of the Map's values.
     * @return The Map.
     */
    private static <T> Map<String, T> conversionMap(final CategoricalValueSchema categoricalValueSchema,
                                                    final Function<Integer, T> indexToValue) {

        final List<String> nominalValues = ImmutableList.copyOf(categoricalValueSchema.getNominalValues());

        return IntStream.range(0, nominalValues.size())
                .boxed()
                .collect(Collectors.toMap(nominalValues::get, indexToValue));
    }

    /**
     * According to the prebuilt encodings, transforms the serializable value corresponding to the given field index
     * to the appropriate value.
     * <p>
     * Failed conversions will result in {@link Double#NaN}.
     *
     * @param value The value to convert.
     * @param index The 0-based index of the field the value is coming from.
     * @return The value encoded as double (in case of a Numeric or Categorical field) or as a String (in case of a String field).
     */
    public Serializable encode(final Serializable value, final int index) {
        return this.encoders.get(index).apply(value);
    }

    /**
     * The number of fields known by the encoder.
     *
     * @return The number of fields.
     */
    public int numberFields() {
        return this.encoders.size();
    }

    /**
     * Wraps encoding functions to handle null field values or failed conversions.
     *
     * @param value          The value to convert.
     * @param function       The encoding function.
     * @param coalescedValue The value to return in case {@code value} is null or the encoding function fails.
     * @return The converted field value if non-null and conversion is successful. Null otherwise.
     * @since 0.1.0
     */
    private static Object handleNullOrFailed(final Serializable value,
                                             final SerializableEncoder function,
                                             final Object coalescedValue) {
        try {
            return value == null ? coalescedValue : function.apply(value);
        } catch (final Exception e) {
            // Not logging on purpose since this may be called a LOT of times
            return coalescedValue;
        }
    }

    /**
     * Interface to ensure that all converters are Serializable so that this class is too.
     */
    public interface SerializableEncoder extends Serializable, Function<Serializable, Serializable> {

    }
}
