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
package com.feedzai.util.algorithm;

import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.google.common.collect.ImmutableSet;

import static com.feedzai.util.algorithm.MLAlgorithmEnum.createDescriptor;

/**
 * Specifies the algorithms provided by a generic provider.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
public enum GenericAlgorithm implements MLAlgorithmEnum {

    /**
     * Generic classification algorithm.
     * Models that use this algorithm should implement the classify and getClassDistribution function.
     */
    GENERIC_CLASSIFICATION(createDescriptor(
            "Generic Classification",
            ImmutableSet.of(),
            MachineLearningAlgorithmType.MULTI_CLASSIFICATION,
            "http://feedzai.com"
    ));

    /**
     * {@link MLAlgorithmDescriptor} for this algorithm.
     */
    public final MLAlgorithmDescriptor descriptor;

    /**
     * Constructor.
     *
     * @param descriptor {@link MLAlgorithmDescriptor} for this algorithm.
     */
    GenericAlgorithm(final MLAlgorithmDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public MLAlgorithmDescriptor getAlgorithmDescriptor() {
        return this.descriptor;
    }
}
