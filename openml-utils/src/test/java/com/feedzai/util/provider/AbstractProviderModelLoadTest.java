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

package com.feedzai.util.provider;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.feedzai.openml.util.algorithm.MLAlgorithmEnum;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Contains tests for model loading with a Provider.
 *
 * @param <M> The type of a class that extends {@link ClassificationMLModel}.
 * @param <L> The type of a class that extends {@link MachineLearningModelLoader}.
 * @param <P> The type of a class that extends {@link MachineLearningProvider}.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public abstract class AbstractProviderModelLoadTest<M extends ClassificationMLModel,
                                                    L extends MachineLearningModelLoader<M>,
                                                    P extends MachineLearningProvider<L>>
        extends AbstractProviderModelBaseTest<M, L, P> {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                       TESTS                       *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Checks that is possible to load a schema from a valid path.
     *
     * @throws ModelLoadingException If there is an error loading the schema.
     */
    @Test
    public void loadSchemaValidTest() throws ModelLoadingException {
        final L machineLearningModelLoader = getMachineLearningModelLoader(getValidAlgorithm());

        final String modelPath = getClass().getResource(File.separator + getValidModelDirName()).getPath();
        final DatasetSchema datasetSchema = machineLearningModelLoader.loadSchema(Paths.get(modelPath));

        assertThat(datasetSchema)
                .as("the expected datasetSchema")
                .isEqualTo(createDatasetSchema(getFirstModelTargetNominalValues()));
    }

    /**
     * Checks that the load of a schema from a invalid path results in a empty object.
     */
    @Test
    public void loadSchemaInvalidTest() {
        final L machineLearningModelLoader = getMachineLearningModelLoader(getValidAlgorithm());

        final File myTempDir = Files.createTempDir();
        myTempDir.deleteOnExit();

        assertThatThrownBy(() -> machineLearningModelLoader.loadSchema(myTempDir.toPath()))
                .as("Loading Schema Exception")
                .hasMessageContaining("There is no model.json")
                .hasMessageContaining("model path");
    }

    /**
     * Checks that a validations of a valid schema doesn't has errors.
     */
    @Test
    public void validModelValidateTest() {
        final L machineLearningModelLoader = getMachineLearningModelLoader(getValidAlgorithm());

        final List<ParamValidationError> errors = machineLearningModelLoader.validateForLoad(
                getPathToModelDir(),
                createDatasetSchema(getFirstModelTargetNominalValues()),
                Collections.emptyMap()
        );

        assertThat(errors)
                .as("there are not errors")
                .isEmpty();
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                        UTIL                       *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Loads a {@link ClassificationMLModel} for the provided algorithm and filename.
     *
     * @param algorithm    Algorithm of the loaded model.
     * @param dirName      Name of the directory of the model.
     * @param targetValues The nominal values of the target field.
     * @return the loaded {@link ClassificationMLModel}.
     * @throws ModelLoadingException If anything goes wrong.
     */
    protected M loadModel(final MLAlgorithmEnum algorithm,
                          final String dirName,
                          final Set<String> targetValues) throws ModelLoadingException {
        final L modelLoader = getMachineLearningModelLoader(algorithm);
        final String modelPath = getClass().getResource("/" + dirName).getPath();
        return modelLoader.loadModel(
                Paths.get(modelPath),
                createDatasetSchema(targetValues)
        );
    }

    /**
     * Gets a {@link Path} to the directory of a valid model.
     *
     * @return The Path.
     */
    Path getPathToModelDir() {
        return Paths.get(getClass().getResource("/" + getValidModelDirName()).getPath());
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                  ABSTRACT METHODS                 *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Gets a string with a name of the directory that contains a model.
     *
     * @return a name of the directory with the model.
     */
    public abstract String getValidModelDirName();
}
