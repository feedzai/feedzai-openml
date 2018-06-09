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
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Similar to {@link AbstractProviderModelLoadTest} with the difference that also verifies that the target field of a
 * {@link ClassificationMLModel} is categorical.
 *
 * @param <M> The type of a class that extends {@link ClassificationMLModel}.
 * @param <L> The type of a class that extends {@link MachineLearningModelLoader}.
 * @param <P> The type of a class that extends {@link MachineLearningProvider}.
 * @author Paulo Pereira (paulo.pereira@feedzai.com)
 * @since 0.1.0
 */
public abstract class AbstractProviderCategoricalTargetTest<M extends ClassificationMLModel,
                                                            L extends MachineLearningModelLoader<M>,
                                                            P extends MachineLearningProvider<L>>
        extends AbstractProviderModelLoadTest<M, L, P> {

    /**
     * Checks that a validation of an invalid schema (that doesn't have a categorical for the target variable) has errors.
     */
    @Test
    public void noCategoricalTargetValidateTest() {
        final L machineLearningModelLoader = getFirstMachineLearningModelLoader();

        final DatasetSchema datasetSchema = new DatasetSchema(
                0,
                ImmutableList.of(
                        new FieldSchema(
                                "amount",
                                0,
                                new NumericValueSchema(false)
                        )
                )
        );

        final List<ParamValidationError> errors = machineLearningModelLoader.validateForLoad(
                getPathToModelDir(),
                datasetSchema,
                Collections.emptyMap()
        );
        final List<String> errorMessages = errors.stream().map(ParamValidationError::getMessage).collect(Collectors.toList());

        assertThat(errorMessages)
                .as("there should be only one error")
                .hasSize(1);

        assertThat(errorMessages.get(0))
                .as("error message")
                .contains("Target variable")
                .contains("must be a categorical field");
    }
}
