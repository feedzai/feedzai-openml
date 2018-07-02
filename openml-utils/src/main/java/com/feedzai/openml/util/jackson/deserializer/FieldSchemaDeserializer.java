/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 */

package com.feedzai.openml.util.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.FieldSchema;

import java.io.IOException;

/**
 * Custom {@link JsonDeserializer} for known {@link FieldSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class FieldSchemaDeserializer extends StdDeserializer<FieldSchema> {

    /**
     * Name of the json field that contains the name of the field.
     */
    public static final String FIELD_NAME = "fieldName";

    /**
     * Name of the json field that contains the index of the field.
     */
    public static final String FIELD_INDEX = "fieldIndex";

    /**
     * Name of the json field that contains the value of the field.
     */
    public static final String VALUE_SCHEMA = "valueSchema";

    /**
     * Constructor of this object.
     */
    public FieldSchemaDeserializer() {
        this(null);
    }

    /**
     * Constructor of this object.
     *
     * @param vc Type of values that this deserializer handles.
     */
    public FieldSchemaDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public FieldSchema deserialize(final JsonParser jsonParser,
                            final DeserializationContext deserializationContext) throws IOException {

        final JsonNode treeNode = jsonParser.getCodec().readTree(jsonParser);

        final String fieldName = treeNode.get(FIELD_NAME).textValue();
        final int fieldIndex = (int) treeNode.get(FIELD_INDEX).numberValue();

        final TreeNode valueSchemaNode = treeNode.get(VALUE_SCHEMA);
        final AbstractValueSchema valueSchema = jsonParser.getCodec().readValue(
                valueSchemaNode.traverse(jsonParser.getCodec()),
                new TypeReference<AbstractValueSchema>() { }
        );

        return new FieldSchema(fieldName, fieldIndex, valueSchema);
    }
}
