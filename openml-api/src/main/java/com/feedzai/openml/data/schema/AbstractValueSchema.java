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

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.io.Serializable;

/**
 * A schema that specifies the acceptable values for a given field.
 * Besides the acceptable values, a {@link AbstractValueSchema} can also specify if it accepts missing values.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public abstract class AbstractValueSchema implements Serializable {

    /**
     * The missing value representation.
     */
    public static final String MISSING_VALUE = null;

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4963437183478456079L;

    /**
     * A flag indicating whether missing values are allowed.
     */
    private final boolean allowMissing;

    /**
     * Creates a new instance.
     *
     * @param allowMissing A flag that indicates whether missing values shold be allowed.
     */
    protected AbstractValueSchema(final boolean allowMissing) {
        this.allowMissing = allowMissing;
    }

    /**
     * Returns a flag that indicates whether missing values are allowed.
     *
     * @return {@code true} if missing values are allowed, and {@code false} otherwise.
     */
    public boolean isAllowMissing() {
        return this.allowMissing;
    }

    /**
     * Validates an input value according to this schema.
     *
     * @param value The string representation of the value to validate.
     * @return {@code true} if the value matches the schema or {@code false} otherwise.
     */
    public boolean validate(final String value) {
        return !StringUtils.equals(value, AbstractValueSchema.MISSING_VALUE) || this.allowMissing;

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.allowMissing);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractValueSchema other = (AbstractValueSchema) obj;
        return Objects.equals(this.allowMissing, other.allowMissing);
    }

    @Override
    public final String toString() {
        return toStringHelper().toString();
    }

    /**
     * Method for subclasses to override (calling super) to add their properties to the toString generation.
     *
     * @return A {@link MoreObjects.ToStringHelper} with all fields added.
     */
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("allowMissing", this.allowMissing);
    }
}
