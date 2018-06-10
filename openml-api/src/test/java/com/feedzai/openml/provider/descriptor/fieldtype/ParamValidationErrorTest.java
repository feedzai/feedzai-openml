/*
 * The copyright of this file belongs to Feedzai. The file cannot be
 * reproduced in whole or in part, stored in a retrieval system,
 * transmitted in any form, or by any means electronic, mechanical,
 * photocopying, or otherwise, without the prior permission of the owner.
 *
 * (c) 2018 Feedzai, Strictly Confidential
 *
 */

package com.feedzai.openml.provider.descriptor.fieldtype;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple UTs on {@link ParamValidationError}.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.1
 */
public class ParamValidationErrorTest {

    /**
     * Test {@link ParamValidationError#getMessage()} returns the expected value.
     */
    @Test
    public void getMessage() {
        assertThat(new ParamValidationError("full message").getMessage())
                .as("The call to getMessage")
                .isEqualTo("full message");

        assertThat(new ParamValidationError("name", "value", "reason").getMessage())
                .as("The call to getMessage with generated message")
                .contains("name")
                .contains("value")
                .contains("reason");
    }

    /**
     * Tests equality and hashcode.
     */
    @Test
    public void testEquality() {
        final ParamValidationError error1 = new ParamValidationError("full message");
        final ParamValidationError error2 = new ParamValidationError("full message");
        assertThat(error1)
                .isEqualTo(error2);

        assertThat(error1.hashCode())
                .isEqualTo(error2.hashCode());
    }
}
