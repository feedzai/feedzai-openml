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

/**
 * A POJO representing a parameter validation error returned from {@link ModelParameterType#validate(String, String)}.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class ParamValidationError {

    /**
     * The message describing the error.
     */
    private final String message;

    /**
     * Creates a new instance from the error message.
     *
     * @param message The error message.
     */
    public ParamValidationError(final String message) {
        this.message = Preconditions.checkNotNull(message, "message should not be null");
    }

    /**
     * Overload constructor to help creating a new instance from the parameter name and value.
     *
     * @param parameterName The parameter name.
     * @param parameterValue The parameter value.
     * @param reason The reason behind the validation error.
     */
    public ParamValidationError(final String parameterName, final String parameterValue, final String reason) {
        this(String.format("Parameter %s has value %s: %s", parameterName, parameterValue, reason));
    }

    /**
     * Returns the error message.
     *
     * @return The textual representation of the error.
     */
    public String getMessage() {
        return this.message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.message);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ParamValidationError other = (ParamValidationError) obj;
        return Objects.equals(this.message, other.message);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", this.message)
                .toString();
    }
}
