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

import java.util.Objects;
import java.util.Optional;

/**
 * A concrete implementation of a {@link ModelParameterType} for parameters whose values are boolean.
 * Check {@link #validate(String, String)} documentation for valid boolean value representations.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class BooleanFieldType implements ModelParameterType {

    /**
     * Defines whether this field defaults to the value true.
     */
    private final boolean defaultTrue;

    /**
     * Instantiates a new {@link BooleanFieldType}.
     *
     * @param defaultTrue Whether this field defaults to the value true.
     */
    public BooleanFieldType(final boolean defaultTrue) {
        this.defaultTrue = defaultTrue;
    }

    /**
     * Whether this field defaults to the value true.
     *
     * @return A boolean stating whether the default value of this field is true.
     */
    public boolean isDefaultTrue() {
        return this.defaultTrue;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method will accept as valid any case variation of
     * {@code "true"} and {@code "false"} (meaning, it's case insensitive).
     */
    @Override
    public Optional<ParamValidationError> validate(final String parameterName, final String parameterValue) {
        if (null == parameterValue) {
            return Optional.of(new ParamValidationError(parameterName, parameterValue, "should not be null"));
        }

        if (parameterValue.equalsIgnoreCase("true") || parameterValue.equalsIgnoreCase("false")) {
            return Optional.empty();
        } else {
            return Optional.of(new ParamValidationError(parameterName, parameterValue, "is not a valid boolean value"));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.defaultTrue);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BooleanFieldType other = (BooleanFieldType) obj;
        return Objects.equals(this.defaultTrue, other.defaultTrue);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("defaultTrue", this.defaultTrue)
                .toString();
    }
}
