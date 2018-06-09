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

import com.feedzai.openml.data.Dataset;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.model.MachineLearningModelTrainer;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * An example model trainer.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class ExampleModelTrainer extends ExampleModelLoader implements MachineLearningModelTrainer<ExampleModel> {

    /**
     * Model trainer.
     *
     * @param indexToPredict This loader always loads models that predict the field, indicated by this index, to be the
     *                       class of the instances.
     */
    public ExampleModelTrainer(final int indexToPredict) {
        super(indexToPredict);
    }

    @Override
    public ExampleModel fit(final Dataset dataset,
                            final Random random,
                            final Map<String, String> params) {


        return loadModel(null, dataset.getSchema());
    }

    @Override
    public List<ParamValidationError> validateForFit(final Path pathToPersist,
                                                     final DatasetSchema schema,
                                                     final Map<String, String> params) {
        return ImmutableList.of();
    }
}
