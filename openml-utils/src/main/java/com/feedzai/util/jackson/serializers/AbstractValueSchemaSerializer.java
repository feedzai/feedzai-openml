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
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import com.feedzai.util.jackson.deserializer.AbstractValueSchemaDeserializer;

import java.io.IOException;

/**
 * Custom {@link JsonSerializer} for known {@link AbstractValueSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class AbstractValueSchemaSerializer extends StdSerializer<AbstractValueSchema> {

    /**
     * Constructor of this object.
     */
    public AbstractValueSchemaSerializer() {
        this(null);
    }

    /**
     * Constructor of this object.
     *
     * @param t Nominal type supported.
     */
    public AbstractValueSchemaSerializer(final Class<AbstractValueSchema> t) {
        super(t);
    }

    @Override
    public void serialize(final AbstractValueSchema valueSchema,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        if (valueSchema instanceof CategoricalValueSchema) {
            jsonGenerator.writeStringField(AbstractValueSchemaDeserializer.VALUE_TYPE, AbstractValueSchemaDeserializer.CATEGORICAL_TYPE);
            jsonGenerator.writeObjectField(AbstractValueSchemaDeserializer.NOMINAL_VALUES, ((CategoricalValueSchema) valueSchema).getNominalValues());
        } else if (valueSchema instanceof NumericValueSchema) {
            jsonGenerator.writeStringField(AbstractValueSchemaDeserializer.VALUE_TYPE, AbstractValueSchemaDeserializer.NUMERIC_TYPE);
        } else if (valueSchema instanceof StringValueSchema) {
            jsonGenerator.writeStringField(AbstractValueSchemaDeserializer.VALUE_TYPE, AbstractValueSchemaDeserializer.STRING_TYPE);
        } else {
            throw new UnsupportedOperationException(
                    String.format("A unknown field type [%s] was found.", valueSchema)
            );
        }

        jsonGenerator.writeBooleanField(AbstractValueSchemaDeserializer.ALLOW_MISSING, valueSchema.isAllowMissing());
        jsonGenerator.writeEndObject();
    }
}

