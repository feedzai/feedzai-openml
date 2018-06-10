/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 *
 */

package com.feedzai.openml.example;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.util.data.schema.TestDatasetSchemaBuilder;
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
