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

import com.feedzai.openml.provider.descriptor.fieldtype.ChoiceFieldType;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the behaviour of the {@link ChoiceFieldType} class.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class ChoiceFieldTypeTest extends AbstractConfigFieldTypeTest<ChoiceFieldType> {

    @Override
    ChoiceFieldType getInstance() {
        return new ChoiceFieldType(ImmutableSet.of("val1", "val2"), "val1");
    }

    @Override
    ChoiceFieldType getAnotherInstance() {
        return new ChoiceFieldType(ImmutableSet.of("val1", "val2"), "val2");
    }

    /**
     * Tests the {@link ChoiceFieldType#validate(String, String)} method.
     */
    @Test
    public void testValidate() {
        final ChoiceFieldType fieldType = new ChoiceFieldType(ImmutableSet.of("value1", "value2"), "value2");

        assertValidationResult(fieldType, "paramName0", null, true);
        assertValidationResult(fieldType, "paramName1", "value1", false);
        assertValidationResult(fieldType, "paramName2", "value2", false);
        assertValidationResult(fieldType, "paramName3", "Value1", true);
        assertValidationResult(fieldType, "paramName4", "Value2", true);
        assertValidationResult(fieldType, "paramName5", "random", true);
        assertValidationResult(fieldType, "paramName6", "", true);
    }

    /**
     * Tests that the properties of the field type can be retrieved.
     */
    @Test
    public void testPropertiesOfType() {
        final Set<String> possibleValues = ImmutableSet.of("value1", "value2");
        final String defaultValue = "value1";

        final ChoiceFieldType fieldType = new ChoiceFieldType(possibleValues, defaultValue);

        assertThat(fieldType.getAllowedValues())
                .isEqualTo(possibleValues);

        assertThat(fieldType.getDefaultValue())
                .isEqualTo(defaultValue);
    }
}
