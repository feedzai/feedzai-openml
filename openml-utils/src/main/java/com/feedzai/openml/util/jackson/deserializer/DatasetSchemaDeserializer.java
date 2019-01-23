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
import java.util.Optional;

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

        final Optional<Integer> targetIndex = Optional.ofNullable(treeNode.get(TARGET_INDEX))
                .map(JsonNode::intValue);

        final TreeNode fieldNode = treeNode.get(FIELD_SCHEMAS);
        final List<FieldSchema> schemaList = jsonParser.getCodec().readValue(
                fieldNode.traverse(jsonParser.getCodec()),
                new TypeReference<List<FieldSchema>>() { }
        );

        return targetIndex
                .map(index -> new DatasetSchema(index, schemaList))
                .orElse(new DatasetSchema(schemaList));
    }
}
