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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the serialization of {@link DatasetSchema}.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class DatasetSchemaSerializationTest {

    /**
     * Checks that is possible to serialize an instance of {@link DatasetSchema} in a JSON.
     *
     * @throws IOException If any unexpected error occurs.
     */
    @Test
    public void testJSONSerialization() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SerializersInModule());

        final DatasetSchema datasetSchemaModel = createDatasetSchema();
        final String datasetSchemaJSON = mapper.writeValueAsString(datasetSchemaModel);

        final DatasetSchema deserializableJSONContextModel = mapper.readValue(datasetSchemaJSON, DatasetSchema.class);

        Assert.assertEquals(datasetSchemaModel, deserializableJSONContextModel);
    }

    /**
     * Creates a dummy {@link DatasetSchema} to use in tests.
     *
     * @return a instance of {@link DatasetSchema}.
     */
    private DatasetSchema createDatasetSchema() {
        return TestDatasetSchemaBuilder.builder()
                .withCategoricalFields(2)
                .withNumericalFields(3)
                .withStringFields(1)
                .build();
    }
}
