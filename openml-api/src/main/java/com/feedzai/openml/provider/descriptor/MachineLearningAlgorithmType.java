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

/**
 * Represents the set of possible ML algorithm types.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public enum MachineLearningAlgorithmType {

    /**
     * Represents an algorithm suitable for Binary Classification problems.
     * @deprecated use {@link #SUPERVISED_BINARY_CLASSIFICATION} instead.
     */
    @Deprecated
    BINARY_CLASSIFICATION,

    /**
     * Represents a supervised machine learning algorithm suitable for Binary Classification problems.
     * E.g. - Random Forest (may also be multi-classification)
     */
    SUPERVISED_BINARY_CLASSIFICATION,

    /**
     * Represents an Anomaly Detection algorithm.
     * E.g. Isolation Forest
     */
    ANOMALY_DETECTION,

    /**
     * Represents an algorithm suitable for Multi Classification problems.
     * E.g. - Support Vector Machines
     */
    MULTI_CLASSIFICATION,

    /**
     * Represents an algorithm suitable for Regression problems.
     * E.g. - Logistic regression
     */
    REGRESSION
}
