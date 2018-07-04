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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class used to register Algorithms and generate {@link MLAlgorithmDescriptor} for them.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public interface MLAlgorithmEnum {

    /**
     * Logger.
     */
    Logger logger = LoggerFactory.getLogger(MLAlgorithmEnum.class);

    /**
     * Utility function to create a new {@link MLAlgorithmDescriptor}.
     *
     * @param algorithmName       Name of the algorithm.
     * @param algorithmParameters Set of the parameters of the training algorithm.
     * @param algorithmType       The {@link MachineLearningAlgorithmType algorithm type} that describes the suitable
     *                            problems for this algorithm.
     * @param documentationLink   A link to the documentation of the algorithm.
     * @return The {@link MLAlgorithmDescriptor}.
     */
    static MLAlgorithmDescriptor createDescriptor(final String algorithmName,
                                                  final Set<ModelParameter> algorithmParameters,
                                                  final MachineLearningAlgorithmType algorithmType,
                                                  final String documentationLink) {
        URL documentationUrl = null;
        try {
            documentationUrl = new URL(documentationLink);
        } catch (final MalformedURLException e) {
            logger.warn(String.format("The documentation URL for the algorithm descriptor '%s' is malformed", algorithmName), e);
        }

        return new MLAlgorithmDescriptor(
                algorithmName,
                algorithmParameters,
                algorithmType,
                documentationUrl
        );
    }

    /**
     * Getter for the Algorithm Descriptor each Enum instance holds.
     * In an ideal world this interface would be a superclass and would hold this property.
     *
     * @return The {@link MLAlgorithmDescriptor} held by this enum instance holds.
     */
    MLAlgorithmDescriptor getAlgorithmDescriptor();

    /**
     * Getter for the algorithm's name.
     *
     * @return The algorithm's name.
     */
    default String getName() {
        return this.getAlgorithmDescriptor().getAlgorithmName();
    }



    /**
     * Getter for the set of descriptors for this algorithm enum.
     *
     * @param values Possible values for the enum (required as overriding static methods is not possible).
     * @return Set of descriptors for this algorithm enum.
     */
    static Set<MLAlgorithmDescriptor> getDescriptors(final MLAlgorithmEnum[] values) {
        return Arrays.stream(values)
                .map(MLAlgorithmEnum::getAlgorithmDescriptor)
                .collect(Collectors.toSet());
    }

    /**
     * Getter for an algorithm based on its name.
     *
     * @param <T>    Enum implementation of this interface.
     * @param values Possible values for the enum (required as overriding static methods is not possible).
     * @param name   Name of the algorithm to find.
     * @return An optional containing the algorithm with name {@code name} if it exists.
     */
    static <T extends MLAlgorithmEnum> Optional<T> getByName(final T[] values, final String name) {
        return Arrays.stream(values)
                .filter(value -> value.getName().equals(name))
                .findFirst();
    }
}
