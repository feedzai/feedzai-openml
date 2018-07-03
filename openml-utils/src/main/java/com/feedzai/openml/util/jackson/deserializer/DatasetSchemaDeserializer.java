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
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;

import java.io.IOException;
import java.util.List;

/**
 * Custom {@link JsonDeserializer} for known {@link DatasetSchema} instances.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchemaDeserializer extends StdDeserializer<DatasetSchema> {

    /**
     * Name of the json field that contains the index of the target field.
     */
    public static final String TARGET_INDEX = "targetIndex";

    /**
     * Name of the json field that contains a list with the fields used by the schema.
     */
    public static final String FIELD_SCHEMAS = "fieldSchemas";

    /**
     * Constructor of this object.
     */
    public DatasetSchemaDeserializer() {
        this(null);
    }

    /**
     * Constructor of this object.
     *
     * @param vc Type of values that this deserializer handles.
     */
    public DatasetSchemaDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public DatasetSchema deserialize(final JsonParser jsonParser,
                                     final DeserializationContext deserializationContext) throws IOException {
        final JsonNode treeNode = jsonParser.getCodec().readTree(jsonParser);

        final int targetIndex = (int) treeNode.get(TARGET_INDEX).numberValue();

        final TreeNode fieldNode = treeNode.get(FIELD_SCHEMAS);
        final List<FieldSchema> schemaList = jsonParser.getCodec().readValue(
                fieldNode.traverse(jsonParser.getCodec()),
                new TypeReference<List<FieldSchema>>() { }
        );

        return new DatasetSchema(targetIndex, schemaList);
    }
}
