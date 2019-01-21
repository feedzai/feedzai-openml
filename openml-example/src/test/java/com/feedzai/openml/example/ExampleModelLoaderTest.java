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

package com.feedzai.openml.example;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.util.data.schema.TestDatasetSchemaBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.nio.file.Path;

import static com.google.common.collect.ImmutableMap.of;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Simple validations on the {@link ExampleModelLoader}.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.1
 */
public class ExampleModelLoaderTest {

    /**
     * Tests loading a model.
     */
    @Test
    public void loadModel() {
        final ExampleModelLoader loader = new ExampleModelLoader(0);
        final Path modelPath = get("dummy");
        final DatasetSchema testSchema = TestDatasetSchemaBuilder.builder().withCategoricalFields(1).build();
        assertThat(loader.loadModel(modelPath, testSchema))
                .as("The result loading a model with the Example Loader")
                .isNotNull();
    }

    /**
     * Tests that {@link ExampleModelLoader} cannot load model with a schema that has no target variable.
     */
    @Test
    public final void testLoadModelWithNoSchema() {
        final ExampleModelLoader loader = new ExampleModelLoader(0);
        final Path modelPath = get("dummy");
        final DatasetSchema testSchema = new DatasetSchema(ImmutableList.of());
        assertThatThrownBy(() -> loader.loadModel(modelPath, testSchema))
                .as("The of loading a model with a dataset which has no target variable.")
                .isInstanceOf(IllegalArgumentException.class);

    }

    /**
     * Checks that the {@link ExampleModelLoader}'s validation is a NOOP.
     */
    @Test
    public void validateForLoad() {
        final ExampleModelLoader loader = new ExampleModelLoader(0);
        final Path modelPath = get("dummy");
        final DatasetSchema testSchema = TestDatasetSchemaBuilder.builder().withCategoricalFields(1).build();
        assertThat(loader.validateForLoad(modelPath, testSchema, of()))
                .as("The result of calling a validation on the Example Loader")
                .isEmpty();
    }

    /**
     * Tests that the {@link ExampleModelLoader} does not allow loading schemas from models.
     */
    @Test
    public void loadSchema() {
        final ExampleModelLoader loader = new ExampleModelLoader(0);
        assertThatThrownBy(() -> loader.loadSchema(get("dummypath")))
                .as("Attempting to load a schema")
                .isInstanceOf(ModelLoadingException.class);
    }
}
