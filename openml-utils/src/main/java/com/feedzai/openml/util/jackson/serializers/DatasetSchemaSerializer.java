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

package com.feedzai.openml.util.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.util.jackson.deserializer.DatasetSchemaDeserializer;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom {@link JsonSerializer} for known {@link DatasetSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchemaSerializer extends StdSerializer<DatasetSchema> {

    /**
     * Serial id for this class.
     */
    private static final long serialVersionUID = 6146242224085830807L;

    /**
     * Constructor of this object.
     */
    public DatasetSchemaSerializer() {
        this(null);
    }

    /**
     * Constructor of this object.
     *
     * @param t Nominal type supported.
     */
    public DatasetSchemaSerializer(final Class<DatasetSchema> t) {
        super(t);
    }

    @Override
    public void serialize(final DatasetSchema datasetSchema,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        final Optional<Integer> targetIndex = datasetSchema.getTargetIndex();
        if (targetIndex.isPresent()) {
            jsonGenerator.writeNumberField(DatasetSchemaDeserializer.TARGET_INDEX, targetIndex.get());
        }
        jsonGenerator.writeObjectField(DatasetSchemaDeserializer.FIELD_SCHEMAS, datasetSchema.getFieldSchemas());
        jsonGenerator.writeEndObject();
    }
}
