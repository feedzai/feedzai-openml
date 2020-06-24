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
import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A concrete implementation of a {@link ModelParameterType} for parameters whose values are restricted
 * by a set of possible values of configuration.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class ChoiceFieldType implements ModelParameterType {

    /**
     * The set of values that this parameter allows.
     */
    private final Set<String> allowedValues;

    /**
     * The default value.
     */
    private final String defaultValue;

    /**
     * Creates a new instance of this class.
     *
     * @param allowedValues The {@link Set} of allowed values for this {@link ChoiceFieldType choice parameter}.
     * @param defaultValue  The default value for this {@link ChoiceFieldType choice parameter}.
     */
    public ChoiceFieldType(final Set<String> allowedValues, final String defaultValue) {
        Preconditions.checkNotNull(allowedValues, "allowedValues can't be null.");
        Preconditions.checkArgument(!allowedValues.isEmpty(), "allowedValues can't be empty.");
        Preconditions.checkNotNull(defaultValue, "defaultValue can't be null.");
        Preconditions.checkArgument(allowedValues.contains(defaultValue));

        this.defaultValue = defaultValue;
        this.allowedValues = ImmutableSet.copyOf(allowedValues);
    }

    /**
     * Gets the set of allowed values for the config parameter.
     *
     * @return A {@link Set} of allowed values.
     */
    public Set<String> getAllowedValues() {
        return this.allowedValues;
    }

    /**
     * Gets the default value of this {@link ChoiceFieldType}.
     *
     * @return The default value.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Optional<ParamValidationError> validate(final String parameterName, final String parameterValue) {

        if (this.allowedValues.contains(parameterValue)) {
            return Optional.empty();
        } else {
            String reason = "should be one of: " + String.join(", ", this.allowedValues);

            return Optional.of(new ParamValidationError(parameterName, parameterValue, reason));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.allowedValues, this.defaultValue);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChoiceFieldType other = (ChoiceFieldType) obj;
        return Objects.equals(this.allowedValues, other.allowedValues)
                && Objects.equals(this.defaultValue, other.defaultValue);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("allowedValues", this.allowedValues)
                .add("defaultValue", this.defaultValue)
                .toString();
    }
}
