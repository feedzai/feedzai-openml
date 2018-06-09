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

package com.feedzai.util.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.util.jackson.SerializersInModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for loading {@link DatasetSchema}.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public final class LoadSchemaUtils {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(LoadSchemaUtils.class);

    /**
     * Name of the file used during the import of a model. This file the data schema used to train the model to be
     * imported.
     *
     * @since 0.1.0
     */
    private static final String SCHEMA_IMPORT_MODEL = "model.json";

    /**
     * A string that identifies a {@link AbstractValueSchema} with a categorical value.
     */
    public static final String CATEGORICAL = "categorical";

    /**
     * A string that identifies a {@link AbstractValueSchema} with a numeric value.
     */
    public static final String NUMERIC = "numeric";

    /**
     * A string that identifies a {@link AbstractValueSchema} with a string value.
     */
    public static final String STRING = "string";

    /**
     * Private constructor for utility class.
     */
    private LoadSchemaUtils() {
    }

    /**
     * Gets a {@link DatasetSchema} from a given path. It assumes that the path points to a json with all fields of the
     * data schema.
     *
     * @param filePath The directory with the binary of the model.
     * @return a {@link DatasetSchema}.
     * @throws ModelLoadingException In case there is an error loading the schema from the model.json file in the path.
     */
    public static DatasetSchema datasetSchemaFromJson(final Path filePath) throws ModelLoadingException {
        if (!Files.isDirectory(filePath)) {
            throw new ModelLoadingException("The path should be a directory");
        }

        final Path jsonFilePath = filePath.resolve(SCHEMA_IMPORT_MODEL);
        if (!Files.exists(jsonFilePath)) {
            throw new ModelLoadingException("There is no model.json file on the model path.");
        }

        try {
            final String jsonContent = new String(Files.readAllBytes(jsonFilePath));

            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new SerializersInModule());

            return mapper.readValue(jsonContent, DatasetSchema.class);
        } catch (final IOException e) {
            final String msg = String.format("Could not load schema for model in path %s due to %s", filePath, e.toString());
            logger.error(msg);
            logger.trace(msg, e);  // Avoid full stack trace as this may get invoked with incomplete path during user writing.
            throw new ModelLoadingException(e.toString());
        }
    }

    /**
     * Gets a string that identifies the type of {@code valueSchema}.
     *
     * @param valueSchema The schema of the value.
     * @return a string that identifies the type of the schema.
     */
    public static String getValueSchemaTypeToString(final AbstractValueSchema valueSchema) {
        if (valueSchema instanceof CategoricalValueSchema) {
            return CATEGORICAL;
        } else if (valueSchema instanceof NumericValueSchema) {
            return NUMERIC;
        } else {
            return STRING;
        }
    }
}
