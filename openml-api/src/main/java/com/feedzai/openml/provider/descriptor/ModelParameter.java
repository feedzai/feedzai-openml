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

import com.feedzai.openml.provider.descriptor.fieldtype.ModelParameterType;
import com.feedzai.openml.model.MachineLearningModel;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * This class denotes a configuration parameter to be specified when training a {@link MachineLearningModel}.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class ModelParameter {

    /**
     * The name of the parameter.
     */
    private final String name;

    /**
     * A description about the meaning/use of the parameter.
     */
    private final String description;

    /**
     * Additional information about how to configure the parameter correctly.
     */
    private final String helperDescription;

    /**
     * A boolean that tells if the field is optional.
     */
    private final boolean isMandatory;

    /**
     * The {@link ModelParameterType data type} of the parameter.
     */
    private final ModelParameterType fieldType;

    /**
     * Creates a new instance.
     *
     * @param name              The name of the parameter.
     * @param description       The description of the parameter.
     * @param helperDescription The helper description.
     * @param isMandatory       If this parameter is mandatory for the algorithm.
     * @param fieldType         The {@link ModelParameterType data type} of this parameter.
     */
    public ModelParameter(final String name,
                          final String description,
                          final String helperDescription,
                          final boolean isMandatory,
                          final ModelParameterType fieldType) {

        this.name = Preconditions.checkNotNull(name, "name cannot be null");
        this.description = Preconditions.checkNotNull(description, "description cannot be null");
        this.helperDescription = Preconditions.checkNotNull(helperDescription, "helper description cannot be null");
        this.isMandatory = isMandatory;
        this.fieldType = Preconditions.checkNotNull(fieldType, "field type cannot be null");
    }

    /**
     * Returns the parameter name.
     *
     * @return The parameter name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the parameter description.
     *
     * @return The parameter description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns an additional information about how to configure the parameter correctly.
     *
     * @return The parameter helper description.
     */
    public String getHelperDescription() {
        return this.helperDescription;
    }

    /**
     * Checks if this parameter is mandatory.
     *
     * @return {@code true} if the parameter is mandatory, {@code false} otherwise.
     */
    public boolean isMandatory() {
        return this.isMandatory;
    }

    /**
     * Returns the {@link ModelParameterType data type} of the parameter.
     *
     * @return The data type of the parameter.
     */
    public ModelParameterType getFieldType() {
        return this.fieldType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description, this.helperDescription, this.isMandatory, this.fieldType);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ModelParameter other = (ModelParameter) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.helperDescription, other.helperDescription)
                && Objects.equals(this.isMandatory, other.isMandatory)
                && Objects.equals(this.fieldType, other.fieldType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", this.name)
                .add("description", this.description)
                .add("helperDescription", this.helperDescription)
                .add("isMandatory", this.isMandatory)
                .add("fieldType", this.fieldType)
                .toString();
    }
}
