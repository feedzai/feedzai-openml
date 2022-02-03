/*
 * Copyright 2022 Feedzai
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

package com.feedzai.openml.model;

import com.feedzai.openml.data.ExplanationsConfig;
import com.feedzai.openml.data.Instance;

/**
 * Interface for explanations algorithm. TODO
 * Bridge
 *
 * @since 1.2.0
 */
public interface ExplanationsAlgorithm<M extends MachineLearningModel, C extends ExplanationsConfig> {
    /**
     * Obtain the feature contribution scores for a particular {@link Instance}.
     *
     * TODO
     * @param instance the {@link Instance}.
     * @return An array of feature contribution scores.
     */
    double[] getFeatureContributions(final M model, final C explanationConfiguration, final Instance instance);
}
