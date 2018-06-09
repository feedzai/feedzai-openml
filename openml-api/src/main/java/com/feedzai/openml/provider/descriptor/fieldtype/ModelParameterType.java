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

import com.feedzai.openml.provider.descriptor.ModelParameter;

import java.util.Optional;

/**
 * The concept for {@link ModelParameter hyper-parameter} types.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface ModelParameterType {

    /**
     * Validates the value for the provided parameter is valid according the specific implementation of this abstract class.
     *
     * @param parameterName The name of the parameter to validate (assumed not to be null).
     * @param parameterValue The value to validate.
     * @return An {@link Optional} containing a {@link ParamValidationError} if the value is not valid or an empty {@link Optional} otherwise.
     */
    Optional<ParamValidationError> validate(String parameterName, String parameterValue);

}
