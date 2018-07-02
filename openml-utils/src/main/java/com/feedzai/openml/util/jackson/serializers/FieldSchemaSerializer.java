/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 */

package com.feedzai.openml.util.jackson.serializers;

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
