/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2022 Feedzai, Strictly Confidential
 */

package com.feedzai.openml.explanations;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

/**
 * FIXME
 *
 * @author Sheng Wang (sheng.wang@feedzai.com)
 */
public final class ExplanationAlgorithmConfig implements Serializable {

    /**
     * The serial UID.
     */
    private static final long serialVersionUID = 5239840870948374374L;

    /**
     * @see #getClassIndex()
     */
    private final int classIndex;

    public ExplanationAlgorithmConfig(final Builder builder) {
        Preconditions.checkNotNull(builder, "builder cannot be null");
        this.classIndex = builder.classIndex;
    }

    /**
     * Gets the class index of the explanation to which this configuration corresponds to.
     *
     * @return The name of the explanation.
     */
    public int getClassIndex() {
        return this.classIndex;
    }

    /**
     * Gets a new {@link Builder} instance.
     *
     * @return A new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.classIndex);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ExplanationAlgorithmConfig other = (ExplanationAlgorithmConfig) obj;
        return Objects.equals(this.classIndex, other.classIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("classIndex", this.classIndex)
                .toString();
    }

    /**
     * A companion class to build new {@link ExplanationAlgorithmConfig} instances.
     */
    public static class Builder {

        /**
         * @see ExplanationAlgorithmConfig#classIndex
         */
        private int classIndex;

        /**
         * Sets the class index of the explanation to which the {@link ExplanationAlgorithmConfig} belongs to.
         *
         * @param classIndex The class index.
         * @return This same {@link Builder} instance.
         */
        public Builder withClassIndex(final int classIndex) {
            this.classIndex = classIndex;

            return this;
        }

        /**
         * Builds a new {@link ExplanationAlgorithmConfig} with this {@link Builder} configurations.
         *
         * @return A new {@link ExplanationAlgorithmConfig}.
         */
        public ExplanationAlgorithmConfig build() {
            return new ExplanationAlgorithmConfig(this);
        }
    }
}
