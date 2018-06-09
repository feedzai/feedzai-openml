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

import com.feedzai.openml.model.MachineLearningModel;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * Utility class for loading {@link MachineLearningModel}.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public final class LoadModelUtils {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(LoadModelUtils.class);

    /**
     * Folder inside the directory of a model to be imported that only contains a file with the binary of the model.
     *
     * @since 0.1.0
     */
    public static final String MODEL_FOLDER = "model";

    /**
     * Private constructor for utility class.
     */
    private LoadModelUtils() {
    }

    /**
     * Gets the absolute path of the file with the binary of the model to be imported.
     *
     * @param modelDirectoryPath Path of the directory with the json file and binary of the model.
     * @return The path of the file with the binary of the model.
     * @throws ModelLoadingException If anything goes wrong.
     */
    public static Path getModelFilePath(final Path modelDirectoryPath) throws ModelLoadingException {

        final File modelDirectoryFile = modelDirectoryPath.toFile();
        if (!modelDirectoryFile.exists() || modelDirectoryFile.isFile()) {
            final String errorMsg = String.format("The path [%s] should be a directory", modelDirectoryPath);
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        if (!modelDirectoryFile.canExecute()) {
            final String errorMsg = String.format("Lacking execute permissions in directory [%s]", modelDirectoryPath);
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        final Path binaryDirectoryPath = modelDirectoryPath.resolve(MODEL_FOLDER);
        final File binaryDirectoryFile = binaryDirectoryPath.toFile();

        if (!binaryDirectoryFile.exists() || binaryDirectoryFile.isFile()) {
            final String errorMsg = String.format("The path [%s] should be a directory", binaryDirectoryFile.getAbsoluteFile());
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        if (!binaryDirectoryFile.canExecute() || !binaryDirectoryFile.canRead()) {
            final String errorMsg = String.format("Cannot read the contents of the directory [%s]", binaryDirectoryFile.getAbsoluteFile());
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        final File[] files = binaryDirectoryFile.listFiles();
        if (files == null || files.length != 1) {
            final String errorMsg = String.format("There should be exactly one file inside [%s]", binaryDirectoryFile.getAbsoluteFile());
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        final File modelFile = files[0];
        if (modelFile.isDirectory()) {
            final String errorMsg = String.format("The path [%s] should be a file", modelFile.getAbsoluteFile());
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        if (!modelFile.canRead()) {
            final String errorMsg = String.format("Cannot open the model file for reading [%s]", modelFile.getAbsoluteFile());
            logger.error(errorMsg);
            throw new ModelLoadingException(errorMsg);
        }

        return modelFile.toPath();
    }
}
