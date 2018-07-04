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
