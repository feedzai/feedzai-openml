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

import com.feedzai.openml.provider.descriptor.fieldtype.ModelParameterType;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Generic class holding useful methods for testing implementations of {@link ModelParameterType}.
 *
 * @param <T> The concrete type of the configuration field type being tested.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
abstract class AbstractConfigFieldTypeTest<T extends ModelParameterType> {

    /**
     * Tests the string representation.
     */
    @Test
    public void testToString() {
        assertThat(getInstance().toString())
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Tests the equals check.
     */
    @Test
    public void testEquals() {
        final T instance = getInstance();
        assertThat(instance)
                .isEqualTo(instance)
                .isEqualTo(getInstance())
                .isNotEqualTo(null)
                .isNotEqualTo(getAnotherInstance());
    }

    /**
     * Tests the hashed representation.
     */
    @Test
    public void testHashCode() {
        final T instance = getInstance();
        assertThat(instance.hashCode())
                .isEqualTo(instance.hashCode())
                .isEqualTo(getInstance().hashCode())
                .isNotEqualTo(getAnotherInstance().hashCode());
    }

    /**
     * Gets an instance to test.
     *
     * @return An instance.
     */
    abstract T getInstance();

    /**
     * Gets an instance that is different from {@link #getInstance()}.
     *
     * @return A different instance in terms of properties.
     */
    abstract T getAnotherInstance();

    /**
     * Calls the {@link ModelParameterType#validate(String, String)} method with the given parameter name
     * and value, and asserts if the return type contains an error or not,
     * as expected through the fucntion parameter {@code expectsError}.
     *
     * @param fieldType The concrete implementation of {@link ModelParameterType} to test.
     * @param paramName The name of the parameter.
     * @param paramValue The value of the parameter, as a String.
     * @param expectsError If the validation should return an error.
     */
    void assertValidationResult(final ModelParameterType fieldType,
                                final String paramName,
                                final String paramValue,
                                final boolean expectsError) {

        final Optional<ParamValidationError> result = fieldType.validate(paramName, paramValue);

        assertEquals(
                String.format("Param %s with value %s should %s, error: %s",
                              paramName,
                              paramValue,
                              expectsError ? "not be valid" : "be valid",
                              result.map(ParamValidationError::getMessage).orElse("")),
                result.isPresent(),
                expectsError);
    }
}
