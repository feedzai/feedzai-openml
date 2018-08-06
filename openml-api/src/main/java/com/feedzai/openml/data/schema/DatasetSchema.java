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
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Representation of the data schema. A {@link DatasetSchema} holds information about each field/feature such as
 * its name and type (represented by a {@link FieldSchema}), and the index of the field to predict.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchema implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -6508738272056130831L;

    /**
     * The index of the field to predict.
     */
    private final int targetIndex;

    /**
     * The list of {@link FieldSchema} that represent each feature.
     */
    private final List<FieldSchema> fieldSchemas;

    /**
     * Creates a new instance.
     *
     * @param targetIndex  The index of the target field.
     * @param fieldSchemas The list of {@link FieldSchema} that represent each feature.
     */
    public DatasetSchema(final int targetIndex,
                         final List<FieldSchema> fieldSchemas) {

        this.fieldSchemas = checkFieldSchemas(fieldSchemas);
        this.targetIndex = Preconditions.checkElementIndex(targetIndex, fieldSchemas.size(), "target index should be a valid index");
    }

    /**
     * Auxiliar method to check all the restrictions on the {@link FieldSchema} upon {@link DatasetSchema} creation.
     *
     * @param fieldSchemas The List of field schemas.
     * @return The original list of field schemas if all the checks have passed. Otherwise
     * an {@link IllegalArgumentException} is thrown.
     */
    private  List<FieldSchema> checkFieldSchemas(final List<FieldSchema> fieldSchemas) {

        Preconditions.checkNotNull(fieldSchemas, "field schemas should not be null");

        final List<Integer> indexes = fieldSchemas.stream().map(FieldSchema::getFieldIndex).collect(Collectors.toList());

        // check if indexes are a continuous list with no duplicated elements, i.e., all indexes are unique, are sorted
        // and there are no intermediary indexes missing.
        Preconditions.checkArgument(indexes.equals(IntStream.range(0, indexes.size()).boxed().collect(Collectors.toList())),
                                    "field schemas be sorted by increasing index, with no duplicated nor missing elements");

        Preconditions.checkArgument(
                fieldSchemas.size() == fieldSchemas.stream().map(FieldSchema::getFieldName).collect(Collectors.toSet()).size(),
        "field schemas should have unique names");

        return fieldSchemas;
    }

    /**
     * Gets the index of the target field (the field to predict).
     *
     * @return The target field index.
     */
    public int getTargetIndex() {
        return this.targetIndex;
    }

    /**
     * Gets the description of each feature.
     *
     * @return The list of {@link FieldSchema} representing each feature.
     */
    public List<FieldSchema> getFieldSchemas() {
        return ImmutableList.copyOf(this.fieldSchemas);
    }

    /**
     * Gets the {@link FieldSchema} for the target field.
     *
     * @return The target {@link FieldSchema field schema}.
     */
    public FieldSchema getTargetFieldSchema() {
        return this.fieldSchemas.get(this.targetIndex);
    }

    /**
     * Gets the list of predictive fields (i.e., {@link #getTargetFieldSchema() non target field}).
     *
     * @return the list of predictive fields.
     */
    public List<FieldSchema> getPredictiveFields() {
        return this.fieldSchemas
                .stream()
                .filter(field -> field.getFieldIndex() != this.targetIndex)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.targetIndex, this.fieldSchemas);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DatasetSchema other = (DatasetSchema) obj;
        return Objects.equal(this.targetIndex, other.targetIndex)
                && Objects.equal(this.fieldSchemas, other.fieldSchemas);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("targetIndex", this.targetIndex)
                .add("fieldSchemas", this.fieldSchemas)
                .toString();
    }
}
