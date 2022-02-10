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

package com.feedzai.openml.explanations;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.model.MachineLearningModel;

/**
 * A base class for explanation algorithms. Holds any {@link MachineLearningModel} and can compute the
 * feature contributes for its predictions through {@link #getFeatureContributions(Instance)}.
 *
 * @param <M> Each {@link ModelExplainer} must be associated with an implementation of {@link MachineLearningModel}.
 * @since 1.1.1
 */
public interface ModelExplainer<M extends MachineLearningModel> {

    /**
     * Gets the feature contribution scores for a particular {@link Instance}.
     *
     * @param instance the {@link Instance}.
     * @return An array of feature contribution scores.
     */
    double[] getFeatureContributions(final Instance instance);
}
