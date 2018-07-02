/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 */
package com.feedzai.openml.util.algorithm;

import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.google.common.collect.ImmutableSet;

import static com.feedzai.openml.util.algorithm.MLAlgorithmEnum.createDescriptor;

/**
 * Specifies the algorithms provided by a generic provider.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
public enum GenericAlgorithm implements MLAlgorithmEnum {

    /**
     * Generic classification algorithm.
     * Models that use this algorithm should implement the classify and getClassDistribution function.
     */
    GENERIC_CLASSIFICATION(createDescriptor(
            "Generic Classification",
            ImmutableSet.of(),
            MachineLearningAlgorithmType.MULTI_CLASSIFICATION,
            "http://feedzai.com"
    ));

    /**
     * {@link MLAlgorithmDescriptor} for this algorithm.
     */
    public final MLAlgorithmDescriptor descriptor;

    /**
     * Constructor.
     *
     * @param descriptor {@link MLAlgorithmDescriptor} for this algorithm.
     */
    GenericAlgorithm(final MLAlgorithmDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public MLAlgorithmDescriptor getAlgorithmDescriptor() {
        return this.descriptor;
    }
}
