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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for the serialization of {@link DatasetSchema}.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
@RunWith(Parameterized.class)
public class DatasetSchemaSerializationTest {

    @Parameters
    public static Collection<Object[]> data() {
        final DatasetSchema schema = TestDatasetSchemaBuilder.builder()
                .withCategoricalFields(2)
                .withNumericalFields(3)
                .withStringFields(1)
                .build();

        return Arrays.asList(new Object[][] {
                {schema}, {new DatasetSchema(schema.getFieldSchemas())}
        });
    }

    @Parameter
    public DatasetSchema schema;

    /**
     * Checks that is possible to serialize an instance of {@link DatasetSchema} in a JSON.
     *
     * @throws IOException If any unexpected error occurs.
     */
    @Test
    public void testJSONSerialization() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SerializersInModule());

        final String datasetSchemaJSON = mapper.writeValueAsString(this.schema);

        final DatasetSchema deserializableJSONContextModel = mapper.readValue(datasetSchemaJSON, DatasetSchema.class);

        Assert.assertEquals(this.schema, deserializableJSONContextModel);
    }

}
