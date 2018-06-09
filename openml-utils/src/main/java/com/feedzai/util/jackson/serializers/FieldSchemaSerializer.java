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

package com.feedzai.util.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.feedzai.openml.data.schema.FieldSchema;

import java.io.IOException;

/**
 * Custom {@link JsonSerializer} for known {@link FieldSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class FieldSchemaSerializer extends StdSerializer<FieldSchema> {

    /**
     * Constructor of this object.
     */
    public FieldSchemaSerializer() {
        this(null);
    }

    /**
     * Constructor of this object.
     *
     * @param t Nominal type supported.
     */
    public FieldSchemaSerializer(final Class<FieldSchema> t) {
        super(t);
    }

    @Override
    public void serialize(final FieldSchema fieldSchema,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("fieldIndex", fieldSchema.getFieldIndex());
        jsonGenerator.writeStringField("fieldName", fieldSchema.getFieldName());
        jsonGenerator.writeObjectField("valueSchema", fieldSchema.getValueSchema());
        jsonGenerator.writeEndObject();
    }
}
