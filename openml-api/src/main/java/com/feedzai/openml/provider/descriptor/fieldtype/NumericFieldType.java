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

package com.feedzai.openml.provider.descriptor.fieldtype;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.Optional;

/**
 * A concrete implementation of a {@link ModelParameterType} for parameters whose values are numeric.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class NumericFieldType implements ModelParameterType {

    /**
     * The minimum value allowed.
     */
    private final double minValue;

    /**
     * The maximum value allowed.
     */
    private final double maxValue;

    /**
     * The default value.
     */
    private final double defaultValue;

    /**
     * The type of parameter value that is expected.
     */
    private final ParameterConfigType parameterType;

    /**
     * Creates an instance of this class.
     *
     * @param minValue      The minimum value allowed.
     * @param maxValue      The maximum value allowed.
     * @param parameterType The type of numeric values.
     * @param defaultValue  The default value to be used for this parameter.
     */
    private NumericFieldType(final double minValue,
                             final double maxValue,
                             final ParameterConfigType parameterType,
                             final double defaultValue) {

        Preconditions.checkArgument(minValue <= maxValue, "min value should be smaller or equal to max value");
        Preconditions.checkArgument(
                defaultValue >= minValue && defaultValue <= maxValue,
                "The default value must be within the min and max values."
        );

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.parameterType = Preconditions.checkNotNull(parameterType, "Parameter type can't be null");
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a new {@link NumericFieldType} where the minimum value is the one specified as parameter.
     *
     * @param minValue The minimum value allowed.
     * @param parameterType The type of numeric values.
     * @param defaultValue The default value to be used for this parameter.
     * @return A new {@link NumericFieldType}.
     */
    public static NumericFieldType min(final double minValue, final ParameterConfigType parameterType, final double defaultValue) {
        return range(minValue, Double.MAX_VALUE, parameterType, defaultValue);
    }

    /**
     * Creates a new {@link NumericFieldType} where the maximum value is the one specified as parameter.
     *
     * @param maxValue The maximum value allowed.
     * @param parameterType The type of numeric values.
     * @param defaultValue The default value to be used for this parameter.
     * @return A new {@link NumericFieldType}.
     */
    public static NumericFieldType max(final double maxValue, final ParameterConfigType parameterType, final double defaultValue) {
        return range(-Double.MAX_VALUE, maxValue, parameterType, defaultValue);
    }

    /**
     * Creates a new {@link NumericFieldType} with the specified range of allowed values.
     *
     * @param minValue The minimum value allowed.
     * @param maxValue The maximum value allowed.
     * @param parameterType The type of numeric values.
     * @param defaultValue The default value to be used for this parameter.
     *
     * @return A new {@link NumericFieldType}.
     */
    public static NumericFieldType range(final double minValue, final double maxValue, final ParameterConfigType parameterType, final double defaultValue) {
        return new NumericFieldType(minValue, maxValue, parameterType, defaultValue);
    }

    /**
     * Get the minimum admissible value.
     *
     * @return The value.
     */
    public double getMinValue() {
        return this.minValue;
    }

    /**
     * Get the maximum admissible value.
     *
     * @return The value.
     */
    public double getMaxValue() {
        return this.maxValue;
    }

    /**
     * Get the default value for the field.
     *
     * @return The value.
     */
    public double getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Indicates whether the type of accepted numeric values.
     *
     * @return The type of numeric parameter values that are supported.
     */
    public ParameterConfigType getParameterType() {
        return this.parameterType;
    }

    @Override
    public Optional<ParamValidationError> validate(final String parameterName, final String parameterValue) {

        if (null == parameterValue) {
            return Optional.of(new ParamValidationError(parameterName, parameterValue, "parameter value can't be null."));
        }

        try {
            final double parsedValue = Double.parseDouble(parameterValue);

            if (parsedValue < this.minValue) {
                return Optional.of(new ParamValidationError(parameterName, parameterValue, "should be equal or bigger than " + this.minValue));
            }

            if (parsedValue > this.maxValue) {
                return Optional.of(new ParamValidationError(parameterName, parameterValue, "should be equal or smaller than " + this.maxValue));
            }

            return Optional.empty();
        } catch (final NumberFormatException e) {
            return Optional.of(new ParamValidationError(parameterName, parameterValue, "is not a parsable number"));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minValue, this.maxValue, this.defaultValue, this.parameterType);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NumericFieldType other = (NumericFieldType) obj;
        return Objects.equals(this.minValue, other.minValue)
                && Objects.equals(this.maxValue, other.maxValue)
                && Objects.equals(this.defaultValue, other.defaultValue)
                && Objects.equals(this.parameterType, other.parameterType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("minValue", this.minValue)
                .add("maxValue", this.maxValue)
                .add("defaultValue", this.defaultValue)
                .add("parameterType", this.parameterType)
                .toString();
    }

    /**
     * The Parameter type.
     */
    public enum ParameterConfigType {
        /**
         * Type that is used when the values that are expected are always integers.
         */
        INT,
        /**
         * Type that is used when the values that are expected are real (doubles).
         */
        DOUBLE
    }
}
