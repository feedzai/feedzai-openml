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
