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
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;

import java.util.Set;
import java.util.Objects;
import java.util.SortedSet;

/**
 * Represents the type of a feature whose values are a finite set.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class CategoricalValueSchema extends AbstractValueSchema {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 6725548314380017998L;

    /**
     * The sorted set of nominal values.
     */
    private final SortedSet<String> nominalValues;

    /**
     * Creates a new instance.
     *
     * @param allowMissing  A flag that indicates whether missing values should be allowed.
     * @param nominalValues The list of nominal values.
     */
    public CategoricalValueSchema(final boolean allowMissing,
                                  final Set<String> nominalValues) {
        super(allowMissing);
        Preconditions.checkNotNull(nominalValues, "nominal values should not be null");
        this.nominalValues = ImmutableSortedSet.copyOf(nominalValues);
    }

    /**
     * Gets the Sorted Set of nominal values. Both uniqueness and order are ensured due to the data structure used.
     * This ensurance allows clients of this API encode and decode categorical values into more compact representations.
     *
     * @return The sorted set of nominal values.
     */
    public SortedSet<String> getNominalValues() {
        return this.nominalValues;
    }

    @Override
    public boolean validate(final String value) {
        return super.validate(value) && (value == null || this.nominalValues.contains(value));
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(this.nominalValues);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final CategoricalValueSchema other = (CategoricalValueSchema) obj;
        return Objects.equals(this.nominalValues, other.nominalValues);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("nominalValues", this.nominalValues);
    }
}
