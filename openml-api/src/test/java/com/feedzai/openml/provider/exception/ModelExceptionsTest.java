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

package com.feedzai.openml.provider.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the exceptions used in the API.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ModelExceptionsTest {

    /**
     * Tests the training exception.
     */
    @Test
    public void testTrainingException() {
        test(new ModelTrainingException("some error"));
        test(new ModelTrainingException("some error", new Throwable("bla")));
        test(new ModelTrainingException(new Throwable("something")));
    }

    /**
     * Tests the loading exception.
     */
    @Test
    public void testLoadingException() {
        test(new ModelLoadingException("some error"));
        test(new ModelLoadingException("some error", new Throwable("bla")));
        test(new ModelLoadingException(new Throwable("something")));
    }

    /**
     * Tests that the exception has an error available.
     *
     * @param error The exception.
     */
    private void test(final Exception error) {
        assertThat(error.getMessage())
                .isNotNull()
                .isNotEmpty();
    }

}
