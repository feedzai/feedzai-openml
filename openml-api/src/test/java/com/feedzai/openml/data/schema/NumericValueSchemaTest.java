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

package com.feedzai.openml.data.schema;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link NumericValueSchema} validation behaviour.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class NumericValueSchemaTest {

    /**
     * Tests the value validation with an instance that allows missing values.
     */
    @Test
    public void testAllowingMissingValues() {
        final NumericValueSchema valueSchema = new NumericValueSchema(true);

        assertTrue("Missing value should be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
        assertTrue("Validation of a null value should return true", valueSchema.validate(null));
        assertFalse("Non numeric value should not be valid", valueSchema.validate("not a number"));
        assertTrue("Numeric value should be valid", valueSchema.validate("42"));
    }

    /**
     * Tests the value validation with an instance that does not allow missing values.
     */
    @Test
    public void testNotAllowingMissingValues() {
        final NumericValueSchema valueSchema = new NumericValueSchema(false);

        assertFalse("Missing value should not be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
        assertFalse("Validation of a null value should not be allowed", valueSchema.validate(null));
        assertFalse("Non numeric value should not be valid", valueSchema.validate("not a number"));
        assertTrue("Numeric value should be valid", valueSchema.validate("42"));
    }

}
