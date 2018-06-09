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

package com.feedzai.openml.provider.fieldtype;

import com.feedzai.openml.provider.descriptor.fieldtype.BooleanFieldType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the behaviour of the {@link BooleanFieldType} class.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class BooleanFieldTypeTest extends AbstractConfigFieldTypeTest<BooleanFieldType> {

    @Override
    BooleanFieldType getInstance() {
        return new BooleanFieldType(true);
    }

    @Override
    BooleanFieldType getAnotherInstance() {
        return new BooleanFieldType(false);
    }

    /**
     * Tests the {@link BooleanFieldType#validate(String, String)} method.
     */
    @Test
    public void testValidate() {
        final BooleanFieldType fieldType = new BooleanFieldType(true);

        assertValidationResult(fieldType, "paramName0", null, true);
        assertValidationResult(fieldType, "paramName1", "true", false);
        assertValidationResult(fieldType, "paramName2", "false", false);
        assertValidationResult(fieldType, "paramName3", "False", false);
        assertValidationResult(fieldType, "paramName4", "True", false);
        assertValidationResult(fieldType, "paramName5", "random", true);
        assertValidationResult(fieldType, "paramName6", "", true);
    }

    /**
     * Tests that the default value.
     */
    @Test
    public void testDefaultValue() {
        assertThat(new BooleanFieldType(true).isDefaultTrue())
                .isTrue();

        assertThat(new BooleanFieldType(false).isDefaultTrue())
                .isFalse();
    }
}
