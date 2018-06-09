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

package com.feedzai.util.validate;

import com.feedzai.util.load.LoadModelUtils;
import com.feedzai.util.validate.ValidationUtils;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performs unit tests on the {@link ValidationUtils} methods related to path validations.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public class ValidationUtilsDirectoriesTest {

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method accepts the expected structure.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirOK() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        createTempFile(modelDir);
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a valid directory structure")
                .isEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a file instead of a directory.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirIsFile() throws IOException {
        final Path basefile = createTempFile();
        assertThat(ValidationUtils.validateModelInDir(basefile))
                .as("Validation of a path that is a file")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument does not exist in the filesystem.
     */
    @Test
    public void testValidateModelInDirDoesNotExist() {
        assertThat(ValidationUtils.validateModelInDir(Paths.get("doesnotexist")))
                .as("Validation of a directory structure that doesn't exist")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a directory that does not contain a directory named {@link LoadModelUtils#MODEL_FOLDER}.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirMissingModelDir() throws IOException {
        final Path base = createTempBaseDir();
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the model dir doesn't exist")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} is a file
     * and not a directory.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirModelDirIsFile() throws IOException {
        final Path base = createTempBaseDir();
        Files.createFile(base.resolve(LoadModelUtils.MODEL_FOLDER));
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the model dir is a file")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a directory that cannot be executed by the current user.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirNoPermissionsBaseDir() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        createTempFile(modelDir);
        Files.setPosixFilePermissions(base, ImmutableSet.of());
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the user cannot open the base dir")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} cannot be
     * accessed by the current user.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirNoPermissionsModelDir() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        createTempFile(modelDir);
        Files.setPosixFilePermissions(modelDir, ImmutableSet.of());
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the user cannot open the model dir")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} contains a
     * file the the current user cannot read.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirNoPermissionsModelFile() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        final Path modelfile = createTempFile(modelDir);
        Files.setPosixFilePermissions(modelfile, ImmutableSet.of());
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the user cannot read the file")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} contains
     * more than one file.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirModelDirectoryWithMultipleFiles() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        createTempFile(modelDir);
        createTempFile(modelDir);
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the model dir contains several files")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} is empty.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirEmptyModelDirectory() throws IOException {
        final Path base = createTempBaseDir();
        createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the model dir is empty")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelInDir(Path)} method rejects a structure where the given
     * argument points to a structure where the inner {@link LoadModelUtils#MODEL_FOLDER model directory} contains
     * another directory instead of a file.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelInDirNestedDirectoryInsteadOfFile() throws IOException {
        final Path base = createTempBaseDir();
        final Path modelDir = createDirectory(base.resolve(LoadModelUtils.MODEL_FOLDER));
        createDirectory(modelDir.resolve("subDirShouldBeFile"));
        assertThat(ValidationUtils.validateModelInDir(base))
                .as("Validation of a directory structure where the model dir contains a directory")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelPathToTrain(Path)} method accepts a {@link Path} that points
     * to an empty directory.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelPathToTrainPathEmpty() throws IOException {
        final Path base = createTempBaseDir();
        assertThat(ValidationUtils.validateModelPathToTrain(base))
                .as("Validation of a path that is an empty directory")
                .isEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelPathToTrain(Path)} method rejects a {@link Path} that points
     * to a non-empty directory.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelPathToTrainPathNotEmpty() throws IOException {
        final Path base = createTempBaseDir();
        createTempFile(base);
        assertThat(ValidationUtils.validateModelPathToTrain(base))
                .as("Validation of a path that is not empty")
                .isNotEmpty();
    }

    /**
     * Tests that the {@link ValidationUtils#validateModelPathToTrain(Path)} method rejects a {@link Path} that points
     * to a file instead of a directory.
     *
     * @throws IOException If the test resources could not be created in the filesystem.
     */
    @Test
    public void testValidateModelPathToTrainPathIsFile() throws IOException {
        final Path base = createTempFile();
        assertThat(ValidationUtils.validateModelPathToTrain(base))
                .as("Validation of a path that is not a directory")
                .isNotEmpty();
    }

    /**
     * Creates a directory with a unique name in the system's temporary directory and marks it for deletion once the
     * process ends.
     *
     * @return The {@link Path} to the created dir.
     * @throws IOException If the resource could not be created.
     */
    private Path createTempBaseDir() throws IOException {
        final Path base = Files.createTempDirectory("testmodel");
        base.toFile().deleteOnExit();
        return base;
    }

    /**
     * Creates a directory with the given path and marks it for deletion once the process ends.
     *
     * @param newDir The {@link Path} of the new directory to create.
     * @return The {@link Path} to the created dir.
     * @throws IOException If the resource could not be created.
     */
    private Path createDirectory(final Path newDir) throws IOException {
        final Path directory = Files.createDirectory(newDir);
        directory.toFile().deleteOnExit();
        return directory;
    }

    /**
     * Creates a file with a unique name in the given directory and marks it for deletion once the process ends.
     *
     * @param directory The {@link Path} of the directory where to create the file in.
     * @return The {@link Path} of the created file.
     * @throws IOException If the resource could not be created.
     */
    private Path createTempFile(final Path directory) throws IOException {
        final Path file = Files.createTempFile(directory, "somefile", "tmp");
        file.toFile().deleteOnExit();
        return file;
    }

    /**
     * Creates a file with a unique name in the system's temporary directory and marks it for deletion once the
     * process ends.
     *
     * @return The {@link Path} to the created file.
     * @throws IOException If the resource could not be created.
     */
    private Path createTempFile() throws IOException {
        final Path file = Files.createTempFile("somefile", "tmp");
        file.toFile().deleteOnExit();
        return file;
    }
}
