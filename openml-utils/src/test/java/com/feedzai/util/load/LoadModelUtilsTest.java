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

import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.util.load.LoadModelUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link LoadModelUtils}.
 *
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public class LoadModelUtilsTest {

    /**
     * Tests that is possible to get the path of binary of the model.
     *
     * @throws ModelLoadingException If any error was found.
     */
    @Test
    public void modelPathTest() throws ModelLoadingException {
        final String directoryPath = getClass().getResource("/random_forest").getPath();
        final Path modelFilePath = LoadModelUtils.getModelFilePath(Paths.get(directoryPath));

        assertThat(modelFilePath)
                .as("path of the binary of the model")
                .isEqualTo(Paths.get(directoryPath + "/model/dummy_model"));

    }

    /**
     * Tests that when there is an error with the file of the model, it will throw an exception.
     */
    @Test
    public void notExistTest() {
        Assertions.assertThatThrownBy(() -> LoadModelUtils.getModelFilePath(Paths.get("not_exist")))
                .isInstanceOf(ModelLoadingException.class)
                .hasMessageContaining("should be a directory");
    }
}
