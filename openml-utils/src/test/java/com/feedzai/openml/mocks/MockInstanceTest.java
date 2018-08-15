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

package com.feedzai.openml.mocks;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Class that tests the behaviour of an {@link MockInstance}.
 *
 * @since @@@feedzai.next.release@@@
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 */
public class MockInstanceTest {

    /**
     * Tests the construction through {@link MockInstance#MockInstance(double[])}.
     */
    @Test
    public void testDoubleArray() {

        final double[] doubles = {0, 1, 2};

        final MockInstance instance = new MockInstance(doubles);

        final List<Double> values = Arrays.stream(doubles).boxed().collect(Collectors.toList());

        values.forEach(idx -> {
            assertThat(instance.getValue(idx.intValue()))
                    .as("The instance value")
                    .isEqualTo(idx);

            assertThatThrownBy(() -> instance.getStringValue(idx.intValue()))
                    .as("The instance string value")
                    .isInstanceOf(ClassCastException.class);
        });

    }

    /**
     * Tests the construction through {@link MockInstance#MockInstance(List)}.
     */
    @Test
    public void testList() {

        final List<Serializable> values = ImmutableList.of("0", "1", "2");
        final MockInstance instance = new MockInstance(values);

        values.forEach(idx -> {
            assertThat(instance.getStringValue(Integer.parseInt((String) idx)))
                    .as("The instance string value")
                    .isEqualTo(idx);

            assertThatThrownBy(() -> instance.getValue(Integer.parseInt((String) idx)))
                    .as("The instance value")
                    .isInstanceOf(ClassCastException.class);
        });

    }


    /**
     * Tests the construction through {@link MockInstance#MockInstance(int, Random)}.
     */
    @Test
    public void testSize() {
        final int numberFieldsSize = 10;
        final MockInstance instance = new MockInstance(numberFieldsSize, new Random());

        IntStream.range(0, numberFieldsSize).forEach(idx -> assertThatCode(() -> instance.getValue(idx))
                .as("The instance value for valid index")
                .doesNotThrowAnyException());

        assertThatThrownBy(() -> instance.getValue(numberFieldsSize))
                .as("The instance value for an index bigger than asked")
                .isInstanceOf(IndexOutOfBoundsException.class);

    }

}
