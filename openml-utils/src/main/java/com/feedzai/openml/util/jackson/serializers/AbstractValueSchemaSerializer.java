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
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import com.feedzai.openml.util.jackson.deserializer.AbstractValueSchemaDeserializer;

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

