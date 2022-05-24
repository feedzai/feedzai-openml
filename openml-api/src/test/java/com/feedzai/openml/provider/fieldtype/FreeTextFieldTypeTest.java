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

import com.feedzai.openml.provider.descriptor.fieldtype.FreeTextFieldType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the behaviour of the {@link FreeTextFieldType} class.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class FreeTextFieldTypeTest extends AbstractConfigFieldTypeTest<FreeTextFieldType> {

    /**
     * Tests the {@link FreeTextFieldType#validate(String, String)} method with no regex validator string.
     */
    @Test
    public void validate() {

        final FreeTextFieldType fieldType = new FreeTextFieldType("default");

        assertValidationResult(fieldType, "param0", null, false);
        assertValidationResult(fieldType, "param1", "", false);
        assertValidationResult(fieldType, "param2", "some string", false);
    }

    /**
     * Tests the {@link FreeTextFieldType#validate(String, String)} method using the regex validator constructor.
     */
    @Test
    public void validateRegex() {
        final FreeTextFieldType fieldType = new FreeTextFieldType("1", "^((\\d+(\\.\\d*)?,)*(\\d+(\\.\\d*)?))$");

        // Test valid inputs
        assertValidationResult(fieldType, "param0", "1", false);
        assertValidationResult(fieldType, "param0", "1.", false);
        assertValidationResult(fieldType, "param1", "1.2", false);
        assertValidationResult(fieldType, "param0", "1.,2", false);
        assertValidationResult(fieldType, "param2", "1.2,3,4.5", false);
        assertValidationResult(fieldType, "param3", "1,2,3.4", false);

        // Test invalid inputs
        assertValidationResult(fieldType, "param4", "", true);
        assertValidationResult(fieldType, "param5", "1,", true);
        assertValidationResult(fieldType, "param6", ",1", true);
        assertValidationResult(fieldType, "param6", "1.2,3.,", true);
    }

    /**
     * Checks the default value method.
     */
    @Test
    public void testDefaultValue() {
        final String defaultValue = "default";
        final FreeTextFieldType fieldType = new FreeTextFieldType(defaultValue);
        assertThat(fieldType.getDefaultValue())
                .as("The default value")
                .isEqualTo(defaultValue);
    }

    @Override
    FreeTextFieldType getInstance() {
        return new FreeTextFieldType("default");
    }

    @Override
    FreeTextFieldType getAnotherInstance() {
        return new FreeTextFieldType("another");
    }
}
