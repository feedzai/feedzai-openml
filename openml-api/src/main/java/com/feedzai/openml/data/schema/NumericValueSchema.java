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

/**
 * Represents the type of a feature whose values are numeric (all numeric types up to {@link java.lang.Double}).
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public class NumericValueSchema extends AbstractValueSchema {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -4471978224024450702L;

    /**
     * Creates a new instance.
     *
     * @param allowMissing A flag that indicates whether missing values are allowed.
     */
    public NumericValueSchema(final boolean allowMissing) {
        super(allowMissing);
    }

    @Override
    public boolean validate(final String value) {
        if (!super.validate(value)) {
            return false;
        }

        if (value == null) {
            return true;
        }

        try {
            Double.valueOf(value);
            return true;

        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
