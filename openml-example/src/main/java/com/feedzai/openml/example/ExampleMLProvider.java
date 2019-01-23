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

package com.feedzai.openml.example;

import com.feedzai.openml.model.ClassificationMLModel;
import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import com.feedzai.openml.provider.descriptor.fieldtype.NumericFieldType;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.google.common.collect.ImmutableSet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

/**
 * {@link MachineLearningProvider} for Example purposes.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public class ExampleMLProvider implements MachineLearningProvider<MachineLearningModelLoader<? extends ClassificationMLModel>> {

    /**
     * The reported name of this provider.
     */
    public static final String NAME = "Ultra ML Vendor";

    /**
     * The name of a model that predicts always the first class of a classification problem.
     */
    public static final String PREDICT_FIRST = "Always predicts first class";

    /**
     * The name of a model that predicts always the second class of a classification problem.
     */
    public static final String PREDICT_SECOND = "Always predicts second class";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Set<MLAlgorithmDescriptor> getAlgorithms() {
        final URL documentation;
        try {
            documentation = new URL("http://www.feedzai.com");
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }

        final Set<ModelParameter> parameterList = ImmutableSet.of(new ModelParameter(
                "Not used Parameter",
                "This parameter is not really used at all.",
                "You can fill any number you wish, it is not relevant.",
                true,
                NumericFieldType.range(0, Double.MAX_VALUE, NumericFieldType.ParameterConfigType.DOUBLE, 42.0)
        ));

        return ImmutableSet.of(
                new MLAlgorithmDescriptor(
                        PREDICT_FIRST,
                        parameterList,
                        MachineLearningAlgorithmType.SUPERVISED_BINARY_CLASSIFICATION,
                        documentation
                ),
                new MLAlgorithmDescriptor(
                        PREDICT_SECOND,
                        parameterList,
                        MachineLearningAlgorithmType.MULTI_CLASSIFICATION,
                        documentation
                )
        );
    }

    @Override
    public Optional<MachineLearningModelLoader<? extends ClassificationMLModel>> getModelCreator(final String algorithmName) {
        switch (algorithmName) {
            case PREDICT_FIRST:
                return Optional.of(new ExampleModelLoader(0));
            case PREDICT_SECOND:
                return Optional.of(new ExampleModelLoader(1));
            default:
                return Optional.empty();
        }
    }
}
