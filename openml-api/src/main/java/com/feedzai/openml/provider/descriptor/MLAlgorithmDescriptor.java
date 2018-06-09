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

package com.feedzai.openml.provider.descriptor;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.net.URL;
import java.util.Set;

/**
 * A class that serves the purpose of describing a Machine Learning algorithm and its configuration
 * parameters for training purposes.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class MLAlgorithmDescriptor {

    /**
     * The name of the training algorithm.
     */
    private final String algorithmName;

    /**
     * The set of parameters of the training algorithm.
     */
    private final Set<ModelParameter> parameters;

    /**
     * The {@link MachineLearningAlgorithmType} of the training algorithm.
     */
    private final MachineLearningAlgorithmType algorithmType;

    /**
     * A {@link URL} ponting to the documentation about the algorithm.
     */
    private final URL documentation;

    /**
     * Constructor for a {@link MLAlgorithmDescriptor}.
     *
     * @param algorithmName     The name of the training algorithm.
     * @param parameters        The list of parameters of the training algorithm.
     * @param algorithmType     The {@link MachineLearningAlgorithmType algorithm type} that describes the suitable problems for this algorithm.
     * @param documentation     An {@link URL} pointing to the documentation about the algorithm.
     */
    public MLAlgorithmDescriptor(final String algorithmName,
                                 final Set<ModelParameter> parameters,
                                 final MachineLearningAlgorithmType algorithmType,
                                 final URL documentation) {

        this.algorithmName = Preconditions.checkNotNull(algorithmName, "Algorithm name cannot be null");
        this.parameters = Preconditions.checkNotNull(parameters, "parameters cannot be null");
        this.algorithmType = Preconditions.checkNotNull(algorithmType, "algorithmType cannot be null");
        this.documentation = Preconditions.checkNotNull(documentation, "documentation cannot be null");
    }

    /**
     * Gets the Machine Learning algorithm name.
     *
     * @return A string representation of the algorithm name.
     */
    public String getAlgorithmName() {
        return this.algorithmName;
    }

    /**
     * Gets the set of configurable parameters of the Machine Learning algorithm.
     *
     * @return The set of configurable parameters for the particular ML algorithm.
     */
    public Set<ModelParameter> getParameters() {
        return this.parameters;
    }

    /**
     * Gets the Machine Learning algorithm type.
     *
     * @return The {@link MachineLearningAlgorithmType} representation.
     */
    public MachineLearningAlgorithmType getAlgorithmType() {
        return this.algorithmType;
    }

    /**
     * Gets the URL pointing to the documentation about this algorithm.
     *
     * @return The {@link URL} containing the documentation.
     */
    public URL getDocumentation() {
        return this.documentation;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.algorithmName, this.parameters, this.algorithmType, this.documentation);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MLAlgorithmDescriptor other = (MLAlgorithmDescriptor) obj;
        return Objects.equal(this.algorithmName, other.algorithmName)
                && Objects.equal(this.parameters, other.parameters)
                && Objects.equal(this.algorithmType, other.algorithmType)
                && Objects.equal(this.documentation, other.documentation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("algorithmName", algorithmName)
                .add("parameters", parameters)
                .add("algorithmType", algorithmType)
                .add("documentation", documentation)
                .toString();
    }
}
