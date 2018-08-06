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

import com.feedzai.openml.provider.descriptor.fieldtype.NumericFieldType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests the behaviour of the {@link NumericFieldType} class.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class NumericFieldTypeTest extends AbstractConfigFieldTypeTest<NumericFieldType> {

    @Override
    NumericFieldType getInstance() {
        return NumericFieldType.range(
                0,
                42,
                NumericFieldType.ParameterConfigType.INT,
                13
        );
    }

    @Override
    NumericFieldType getAnotherInstance() {
        return NumericFieldType.min(0.0, NumericFieldType.ParameterConfigType.DOUBLE, 42.42);
    }

    /**
     * Tests the {@link NumericFieldType#validate(String, String)} method.
     */
    @Test
    public void testValidate() {

        final int maxValue = 5;
        final int minValue = -5;
        final int defaultValue = 0;

        final NumericFieldType fieldType = NumericFieldType.range(
                minValue,
                maxValue,
                NumericFieldType.ParameterConfigType.DOUBLE,
                defaultValue
        );

        assertValidationResult(fieldType, "param", null, true);
        assertValidationResult(fieldType, "param", "", true);
        assertValidationResult(fieldType, "param", "non-parsable", true);

        assertValidationResult(fieldType, "param", String.valueOf(minValue - 1), true);
        assertValidationResult(fieldType, "param", String.valueOf(minValue), false);
        assertValidationResult(fieldType, "param", String.valueOf(minValue + 1), false);

        assertValidationResult(fieldType, "param", String.valueOf(maxValue - 1), false);
        assertValidationResult(fieldType, "param", String.valueOf(maxValue), false);
        assertValidationResult(fieldType, "param", String.valueOf(maxValue + 1), true);
    }

    /**
     * Tests that the properties can be fetched correctly.
     */
    @Test
    public void testPropertiesOfType() {
        final double maxValue = 90.0;
        final double defaultValue = 1.0;
        final NumericFieldType type1 =
                NumericFieldType.max(maxValue, NumericFieldType.ParameterConfigType.DOUBLE, defaultValue);

        assertThat(type1.getDefaultValue())
                .isEqualTo(defaultValue);

        assertThat(type1.getMaxValue())
                .isEqualTo(maxValue);

        assertThat(type1.getMinValue())
                .isEqualTo(-Double.MAX_VALUE);

        assertThat(type1.getParameterType())
                .isEqualTo(NumericFieldType.ParameterConfigType.DOUBLE);
    }

    @Test
    public void validateConstructor() {

        assertThatThrownBy(() ->  NumericFieldType.range(-1, 1, null, 0))
                .as("A NumericFieldType cannot have a null parameter type")
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> NumericFieldType.range(1, -1, NumericFieldType.ParameterConfigType.DOUBLE, 1))
                .as("The min value cannpt be bigger than the max value")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatCode(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, 0))
                .as("A valid NumericFieldType constructor")
                .doesNotThrowAnyException();

        assertThatCode(() -> NumericFieldType.range(-1, -1, NumericFieldType.ParameterConfigType.DOUBLE, -1))
                .as("A  NumericFieldType with a max value equal to min")
                .doesNotThrowAnyException();

        assertThatCode(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, -1))
                .as("A  NumericFieldType with a default value equal to min")
                .doesNotThrowAnyException();

        assertThatCode(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, 0))
                .as("A  NumericFieldType with a default value between min and max")
                .doesNotThrowAnyException();

        assertThatCode(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, 1))
                .as("A  NumericFieldType with a default value equal to max")
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, -2))
                .as("A  NumericFieldType with a default value smaller than min")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> NumericFieldType.range(-1, 1, NumericFieldType.ParameterConfigType.DOUBLE, 2))
                .as("A  NumericFieldType with a default value bigger than max")
                .isInstanceOf(IllegalArgumentException.class);

    }

}
