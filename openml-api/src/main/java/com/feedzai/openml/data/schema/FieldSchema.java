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
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.io.Serializable;

/**
 * The representation of each field/feature on a {@link DatasetSchema}.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class FieldSchema implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -326775681858303214L;

    /**
     * The field name.
     */
    private final String fieldName;

    /**
     * The field index in the containing dataset/instance.
     */
    private final int fieldIndex;

    /**
     * The schema used for value validation.
     */
    private final AbstractValueSchema valueSchema;

    /**
     * Creates a new instance.
     *
     * @param fieldName   The field name.
     * @param fieldIndex  The field index in the containing dataset/instance.
     * @param valueSchema The schema used for value validation.
     */
    public FieldSchema(final String fieldName,
                       final int fieldIndex,
                       final AbstractValueSchema valueSchema) {

        Preconditions.checkArgument(fieldIndex >= 0, "field index should be non negative nor empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "field name should not be null  nor empty");
        Preconditions.checkNotNull(valueSchema, "valueSchema should not be null");

        this.fieldIndex = fieldIndex;
        this.fieldName = fieldName;
        this.valueSchema = valueSchema;
    }

    /**
     * Gets the name of this feature.
     *
     * @return the name.
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Gets the position of this field.
     *
     * @return the zero-based position of this field.
     */
    public int getFieldIndex() {
        return this.fieldIndex;
    }

    /**
     * Gets the {@link AbstractValueSchema schema} of this feature.
     *
     * @return the schema.
     */
    public AbstractValueSchema getValueSchema() {
        return this.valueSchema;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.fieldName, this.fieldIndex, this.valueSchema);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FieldSchema other = (FieldSchema) obj;
        return Objects.equals(this.fieldName, other.fieldName)
                && Objects.equals(this.fieldIndex, other.fieldIndex)
                && Objects.equals(this.valueSchema, other.valueSchema);
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", this.fieldName)
                .add("fieldIndex", this.fieldIndex)
                .add("valueSchema", this.valueSchema)
                .toString();
    }
}
