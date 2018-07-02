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
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.util.jackson.deserializer.DatasetSchemaDeserializer;

import java.io.IOException;

/**
 * Custom {@link JsonSerializer} for known {@link DatasetSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchemaSerializer extends StdSerializer<DatasetSchema> {

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
        jsonGenerator.writeNumberField(DatasetSchemaDeserializer.TARGET_INDEX, datasetSchema.getTargetIndex());
        jsonGenerator.writeObjectField(DatasetSchemaDeserializer.FIELD_SCHEMAS, datasetSchema.getFieldSchemas());
        jsonGenerator.writeEndObject();
    }
}
