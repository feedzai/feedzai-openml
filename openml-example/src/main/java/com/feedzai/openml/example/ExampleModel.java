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

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.model.ClassificationMLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Example implementation of a model.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ExampleModel implements ClassificationMLModel {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ExampleModel.class);

    /**
     * The schema used to "train" this model.
     */
    private final DatasetSchema trainSchema;

    /**
     * This model always predicts the field, indicated by this index, to be the class of the instances.
     */
    private final int indexToPredict;

    /**
     * Prediction to return every time we are asked for a class distribution.
     */
    private final double[] prediction;

    /**
     * Constructor.
     *
     * @param trainSchema    The schema used to "train" this model.
     * @param indexToPredict This model always predicts the field, indicated by this index, to be the class of the
     *                       instances.
     * @param prediction     Prediction to return every time we are asked for a class distribution.
     */
    ExampleModel(final DatasetSchema trainSchema, final int indexToPredict, final double[] prediction) {
        this.trainSchema = trainSchema;
        this.indexToPredict = indexToPredict;
        this.prediction = prediction;
    }

    @Override
    public double[] getClassDistribution(final Instance instance) {
        return this.prediction;
    }

    @Override
    public int classify(final Instance instance) {
        return this.indexToPredict;
    }

    @Override
    public boolean save(final Path dir, final String name) {
        try {
            return dir.toFile().createNewFile();
        } catch (final IOException e) {
            final String msg = String.format("Error saving the model into %s", dir);
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public DatasetSchema getSchema() {
        return this.trainSchema;
    }

    @Override
    public void close() throws Exception {

    }
}
