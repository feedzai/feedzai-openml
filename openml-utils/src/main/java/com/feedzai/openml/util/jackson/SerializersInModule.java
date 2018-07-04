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

package com.feedzai.openml.util.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.util.jackson.deserializer.AbstractValueSchemaDeserializer;
import com.feedzai.openml.util.jackson.deserializer.DatasetSchemaDeserializer;
import com.feedzai.openml.util.jackson.deserializer.FieldSchemaDeserializer;
import com.feedzai.openml.util.jackson.serializers.AbstractValueSchemaSerializer;
import com.feedzai.openml.util.jackson.serializers.DatasetSchemaSerializer;
import com.feedzai.openml.util.jackson.serializers.FieldSchemaSerializer;

/**
 * A jackson {@link SimpleModule} that aggregates all the serializers and deserializers that  exist in this module.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class SerializersInModule extends SimpleModule {

    /**
     * Constructor of this object.
     */
    public SerializersInModule() {
        configureForDatasetSchema();
        configureForFieldSchema();
        configureForValueSchema();
    }

    /**
     * Adds the serializers and deserializer of {@link AbstractValueSchema} to the module.
     */
    private void configureForValueSchema() {
        this.addSerializer(AbstractValueSchema.class, new AbstractValueSchemaSerializer());
        this.addDeserializer(AbstractValueSchema.class, new AbstractValueSchemaDeserializer());
    }

    /**
     * Adds the serializers and deserializer of {@link FieldSchema} to the module.
     */
    private void configureForFieldSchema() {
        this.addSerializer(FieldSchema.class, new FieldSchemaSerializer());
        this.addDeserializer(FieldSchema.class, new FieldSchemaDeserializer());
    }

    /**
     * Adds the serializers and deserializer of {@link DatasetSchema} to the module.
     */
    private void configureForDatasetSchema() {
        this.addSerializer(DatasetSchema.class, new DatasetSchemaSerializer());
        this.addDeserializer(DatasetSchema.class, new DatasetSchemaDeserializer());
    }
}
