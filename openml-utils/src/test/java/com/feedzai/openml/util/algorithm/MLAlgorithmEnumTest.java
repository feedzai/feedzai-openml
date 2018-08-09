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

public class MLAlgorithmEnumTest {

    @Test
    public void testCreateDescriptor() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;
        final String documentationLink = "https://feedzai.com/";

        MLAlgorithmDescriptor descriptor = MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, documentationLink);

        assertThat(descriptor.getAlgorithmName())
                .as("")
                .isEqualTo(algoName);

        assertThat(descriptor.getParameters())
                .as("")
                .isEqualTo(parameters);

        assertThat(descriptor.getAlgorithmType())
                .as("")
                .isEqualTo(algorithmType);

        assertThat(descriptor.getDocumentation().toString())
                .as("")
                .isEqualTo(documentationLink);

    }

    @Test
    public void testInvalidCreateDescriptor() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;
        final String documentationLink = "https://feedzai.com/";

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(null, parameters, algorithmType, documentationLink))
                .as("")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, null, algorithmType, documentationLink))
                .as("")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, null, documentationLink))
                .as("")
                .isInstanceOf(NullPointerException.class);


        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, null))
                .as("")
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testInvalidUrl() {
        final String algoName = "algoName";
        final ImmutableSet<ModelParameter> parameters = ImmutableSet.of();
        final MachineLearningAlgorithmType algorithmType = MachineLearningAlgorithmType.BINARY_CLASSIFICATION;

        assertThatThrownBy(() -> MLAlgorithmEnum.createDescriptor(algoName, parameters, algorithmType, "random string"))
                .as("")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetName() {
        assertThat(DummyAlgorithmEnum.DUMMY.getName())
                .as("")
                .isEqualTo(DummyAlgorithmEnum.DUMMY.getAlgorithmDescriptor().getAlgorithmName());
    }

    @Test
    public void testGetDescriptors() {
        Set<MLAlgorithmDescriptor> descriptors = MLAlgorithmEnum.getDescriptors(DummyAlgorithmEnum.values());

        assertThat(descriptors)
                .as("")
                .hasSize(DummyAlgorithmEnum.values().length);

        assertThat(descriptors)
                .as("")
                .isEqualTo(Stream.of(DummyAlgorithmEnum.values()).map(DummyAlgorithmEnum::getAlgorithmDescriptor).collect(Collectors.toSet()));
    }

    @Test
    public void testGetByName() {
        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), null))
        .as("")
        .isEmpty();

        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), "random name"))
                .as("")
                .isEmpty();

        assertThat(MLAlgorithmEnum.getByName(DummyAlgorithmEnum.values(), DummyAlgorithmEnum.DUMMY.getName()))
                .as("")
                .isPresent()
                .contains(DummyAlgorithmEnum.DUMMY);

    }

}