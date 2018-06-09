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

import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * An example model loader.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ExampleModelLoader implements MachineLearningModelLoader<ExampleModel> {

    /**
     * This loader always loads models that predict the field, indicated by this index, to be the class of the
     * instances.
     */
    private final int indexToPredict;

    /**
     * Model loader.
     *
     * @param indexToPredict This loader always loads models that predict the field, indicated by this index, to be the
     *                       class of the instances.
     */
    ExampleModelLoader(final int indexToPredict) {
        this.indexToPredict = indexToPredict;
    }

    @Override
    public ExampleModel loadModel(final Path modelPath, final DatasetSchema schema) {
        final int numberClasses = ((CategoricalValueSchema) schema
                .getFieldSchemas()
                .get(schema.getTargetIndex())
                .getValueSchema())
                .getNominalValues()
                .size();

        final double[] fixedPrediction = new double[numberClasses];
        // the JVM already guarantees that all positions are 0
        // so we change only the one where we predict the class
        fixedPrediction[this.indexToPredict] = 1.0;

        return new ExampleModel(schema, this.indexToPredict, fixedPrediction);
    }

    @Override
    public List<ParamValidationError> validateForLoad(final Path modelPath,
                                                      final DatasetSchema schema,
                                                      final Map<String, String> params) {
        return ImmutableList.of();
    }

    @Override
    public DatasetSchema loadSchema(final Path modelPath) throws ModelLoadingException {
        throw new ModelLoadingException("You cannot load schemas with this provider.");
    }

}
