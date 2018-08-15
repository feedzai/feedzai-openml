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

package com.feedzai.openml.util.algorithm;

import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Class to test the behaviour of {@link MLAlgorithmEnum}.
 *
 * @since @@@feedzai.next.release@@@
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 */
public class MLAlgorithmEnumTest {

    /**
     * Tests the {@link MLAlgorithmEnum#createDescriptor(String, Set, MachineLearningAlgorithmType, String)} with
     * a valid input.
     */
    @Test
    public void testCreateDescriptor() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;
        final String documentationLink = "https://feedzai.com/";

        final MLAlgorithmDescriptor descriptor = MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, documentationLink);

        assertThat(descriptor.getAlgorithmName())
                .as("The descriptor algorithm name")
                .isEqualTo(algoName);

        assertThat(descriptor.getParameters())
                .as("The descriptor parameters")
                .isEqualTo(parameters);

        assertThat(descriptor.getAlgorithmType())
                .as("The descriptor algorithm type")
                .isEqualTo(algorithmType);

        assertThat(descriptor.getDocumentation().toString())
                .as("The descriptor documentation URL")
                .isEqualTo(documentationLink);

    }

    /**
     * Tests the {@link MLAlgorithmEnum#createDescriptor(String, Set, MachineLearningAlgorithmType, String)} with
     * an invalid input.
     */
    @Test
    public void testInvalidCreateDescriptor() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;
        final String documentationLink = "https://feedzai.com/";

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(null, parameters, algorithmType, documentationLink))
                .as("The exception throw by trying to create a descriptor with null algorithm name")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, null, algorithmType, documentationLink))
                .as("The exception throw by trying to create a descriptor with null algorithm params")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, null, documentationLink))
                .as("The exception throw by trying to create a descriptor with null algorithm type")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, null))
                .as("The exception throw by trying to create a descriptor with null URL")
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests the {@link MLAlgorithmEnum#createDescriptor(String, Set, MachineLearningAlgorithmType, String)} with
     * an invalid URL.
     */
    @Test
    public void testInvalidUrl() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, "random string"))
                .as("The exception throw by trying to create a descriptor with an invalid URL")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests the {@link MLAlgorithmEnum#getName}.
     */
    @Test
    public void testGetName() {
        assertThat(DummyAlgorithmEnum.DUMMY.getName())
                .as("The dummy algorithm name")
                .isEqualTo(DummyAlgorithmEnum.DUMMY.getAlgorithmDescriptor().getAlgorithmName());
    }

    /**
     * Tests the {@link MLAlgorithmEnum#getDescriptors(MLAlgorithmEnum[])}.
     */
    @Test
    public void testGetDescriptors() {
        final Set<MLAlgorithmDescriptor> descriptors = MLAlgorithmEnum.getDescriptors(DummyAlgorithmEnum.values());

        assertThat(descriptors)
                .as("The number of descriptors")
                .hasSize(DummyAlgorithmEnum.values().length);

        assertThat(descriptors)
                .as("The descriptors returned by the auxiliary method")
                .isEqualTo(Stream.of(DummyAlgorithmEnum.values()).map(DummyAlgorithmEnum::getAlgorithmDescriptor).collect(Collectors.toSet()));
    }

    /**
     * Tests the {@link MLAlgorithmEnum#getByName(MLAlgorithmEnum[], String)}.
     */
    @Test
    public void testGetByName() {
        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), null))
                .as("The get by name for null argument")
                .isEmpty();

        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), "random name"))
                .as("The get by name for an unexisting name")
                .isEmpty();

        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), DummyAlgorithmEnum.DUMMY.getName()))
                .as("The get by name for an existing name")
                .isPresent()
                .contains(DummyAlgorithmEnum.DUMMY);

    }

}
