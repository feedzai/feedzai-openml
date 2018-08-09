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

package com.feedzai.openml.util;

import com.feedzai.openml.data.schema.*;
import com.feedzai.openml.util.data.ClassificationDatasetSchemaUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for utilities that manage dataset schemas for classification problems.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ClassificationDatasetSchemaUtilTest {

    /**
     * Tests that a valid categorical field as target variable yields the right number of classes.
     */
    @Test
    public void testValidTarget() {
        IntStream.range(0, 10)
                .forEach(numClasses ->
                        assertThat(ClassificationDatasetSchemaUtil.getNumClassValues(createClassificationDatasetSchema(numClasses)))
                                .as("The number of classes")
                                .isEqualTo(numClasses));
    }

    /**
     * Tests that a non-categorical field yields an error as expected.
     */
    @Test
    public void testInvalidTarget() {
        final DatasetSchema datasetSchema = new DatasetSchema(
                0,
                ImmutableList.of(new FieldSchema(
                        "some number",
                        0,
                        new NumericValueSchema(false)
                ))
        );

        assertThatThrownBy(() -> ClassificationDatasetSchemaUtil.getNumClassValues(datasetSchema))
                .as("The error for not having a categorical target")
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testWithCategoricalSchema() {

        final Integer returnValue = 10;

        final Function<CategoricalValueSchema, Integer> fun = (categoricalValueSchema -> returnValue);

        assertThat(ClassificationDatasetSchemaUtil.withCategoricalValueSchema(new NumericValueSchema(true), fun))
                .as("")
                .isEmpty();

        assertThat(ClassificationDatasetSchemaUtil.withCategoricalValueSchema(new StringValueSchema(true), fun))
                .as("")
                .isEmpty();

        assertThat(ClassificationDatasetSchemaUtil.withCategoricalValueSchema(new CategoricalValueSchema(true, ImmutableSet.of()), fun))
                .as("")
                .isNotEmpty()
                .contains(returnValue);
    }

    /**
     * Creates a dataset schema for a classification problem.
     *
     * @param numberClasses The number of classes expected.
     * @return The schema.
     */
    private DatasetSchema createClassificationDatasetSchema(final int numberClasses) {

        final Set<String> possibleValues = IntStream.range(0, numberClasses)
                .mapToObj(idx -> "targetValue" + idx)
                .collect(Collectors.toSet());

        return new DatasetSchema(
                    0,
                    ImmutableList.of(new FieldSchema(
                            "isFraud",
                            0,
                            new CategoricalValueSchema(false, possibleValues)
                    ))
            );
    }

}
