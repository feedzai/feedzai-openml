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
import java.util.regex.Pattern;

/**
 * A concrete implementation of a {@link ModelParameterType} for parameters whose values are textual.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class FreeTextFieldType implements ModelParameterType {

    /**
     * The default value.
     */
    private final String defaultValue;

    /**
     * A regular expression defining a valid input.
     */
    private final Pattern validRegex;

    /**
     * Creates a new instance of this class.
     *
     * @param defaultValue  The default value.
     */
    public FreeTextFieldType(final String defaultValue) {
        this(defaultValue, null);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param defaultValue  The default value.
     * @param validRegex    A regex that matches valid inputs.
     */
    public FreeTextFieldType(final String defaultValue, final String validRegex) {
        this.defaultValue = Preconditions.checkNotNull(defaultValue, "defaultValue can't be null.");
        this.validRegex = validRegex != null ? Pattern.compile(validRegex) : null;
    }

    @Override
    public Optional<ParamValidationError> validate(final String parameterName, final String parameterValue) {
        if (this.validRegex == null) {
            return Optional.empty();
        }

        if (this.validRegex.matcher(parameterValue).matches()) {
            return Optional.empty();
        } else {
            return Optional.of(new ParamValidationError(
                    parameterName,
                    parameterValue,
                    "Should match the following regex: " + this.validRegex
            ));
        }
    }

    /**
     * Gets the default value.
     *
     * @return The default value.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.defaultValue);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FreeTextFieldType other = (FreeTextFieldType) obj;
        return Objects.equals(this.defaultValue, other.defaultValue);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("defaultValue", this.defaultValue)
                .toString();
    }
}
