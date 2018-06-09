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

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link StringValueSchema} validation behaviour.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class StringValueSchemaTest {

    /**
     * A valid value that should be always valid.
     */
    private static final String VALID_VALUE = "valid value";

    /**
     * Tests the value validation with an instance that allows missing values.
     */
    @Test
    public void testValidateAllowMissing() {
        final StringValueSchema valueSchema = new StringValueSchema(true);
        assertTrue("Non missing value should always be valid", valueSchema.validate(VALID_VALUE));

        assertTrue("Missing value should be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
    }

    /**
     * Tests the value validation with an instance that does not allow missing values.
     */
    @Test
    public void testValidateNotAllowMissing() {
        final StringValueSchema valueSchema = new StringValueSchema(false);
        assertTrue("Non missing value should always be valid", valueSchema.validate(VALID_VALUE));

        assertFalse("Missing value should not be valid if specified", valueSchema.validate(AbstractValueSchema.MISSING_VALUE));
    }

}
