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

import com.feedzai.openml.data.Dataset;
import com.feedzai.openml.data.FeatureValues;
import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.PartitionedDataset;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Mock class implementation for a dataset.
 * Used for tests and validation of models.
 * <p>
 * By default creates a schema with {@code fieldSize} fields of numeric type.
 * Creates {@code instanceSize} instances with random values.
 *
 * @author Luis Reis (luis.reis@feedzai.com)
 * @since 0.1.0
 */
public class MockDataset implements Dataset {

    /**
     * Schema of the dataset.
     */
    private final DatasetSchema schema;

    /**
     * List of instances of the dataset.
     */
    private final List<Instance> instances;

    /**
     * Constructor for the dataset that builds a schema filed with numeric fields.
     *
     * @param targetValues The set of possible values for the categorical target variable of the dataset's schema.
     * @param fieldSize    Number of fields the schema should have.
     * @param instanceSize Number of instances the dataset should have.
     * @param random       Random number generator used to generate the instances.
     */
    public MockDataset(final Set<String> targetValues, final int fieldSize, final int instanceSize, final Random random) {
        this(generateDefaultSchema(targetValues, fieldSize), instanceSize, random);
    }

    /**
     * Constructor for the dataset that takes a already existing schema.
     *
     * @param schema       Schema to use in the dataset.
     * @param instanceSize Number of instances the dataset should have.
     * @param random       Random number generator used to generate the instances.
     */
    public MockDataset(final DatasetSchema schema, final int instanceSize, final Random random) {
        this.schema = schema;
        this.instances = generateInstances(instanceSize, random);
    }

    /**
     * Builds a dataset from a given schema and instances.
     *
     * @param schema Schema to use in the dataset.
     * @param instances The List of {@link Instance} the dataset should hold.
     */
    public MockDataset(final DatasetSchema schema, final List<Instance> instances) {
        this.schema = schema;
        this.instances = instances;
    }

    @Override
    public DatasetSchema getSchema() {
        return this.schema;
    }

    @Override
    public Instance instance(final int index) {
        return this.instances.get(index);
    }

    @Override
    public FeatureValues feature(final int index) {
        return null;
    }

    @Override
    public Iterator<Instance> getInstances() {
        return this.instances.iterator();
    }

    @Override
    public Dataset filter(final Predicate<Instance> predicate) {
        return null;
    }

    @Override
    public <K> Map<K, Dataset> groupBy(final Function<Instance, K> function) {
        return null;
    }

    @Override
    public PartitionedDataset partition(final Predicate<Instance> predicate) {
        return new PartitionedDataset() {
            @Override
            public Dataset getMatchedData() {
                return new MockDataset(MockDataset.this.schema, MockDataset.this.instances.stream().filter(predicate).collect(Collectors.toList()));
            }

            @Override
            public Dataset getUnmatchedData() {
                return new MockDataset(MockDataset.this.schema, MockDataset.this.instances.stream().filter(predicate.negate()).collect(Collectors.toList()));
            }
        };
    }

    @Override
    public Dataset empty() {
        return null;
    }

    /**
     * Getter for the number of instances in this dataset.
     *
     * @return The size of the instances array.
     */
    public int getInstancesSize() {
        return this.instances.size();
    }

    /**
     * Function that generates the instances in the constructors above.
     *
     * @param instanceSize Number of instances the dataset should have.
     * @param random       Random number generator used to generate the instances.
     * @return List with the generated instances.
     */
    private List<Instance> generateInstances(final int instanceSize, final Random random) {
        return IntStream.range(0, instanceSize)
                .mapToObj(index -> new MockInstance(this.schema, random))
                .collect(Collectors.toList());
    }

    /**
     * Generates a {@link DatasetSchema} with only numeric fields.
     *
     * @param targetValues The set of possible values for the categorical target variable.
     * @param fieldSize    The number of numeric predictive fields to include in the schema.
     * @return The generated schema.
     */
    public static DatasetSchema generateDefaultSchema(final Set<String> targetValues, final int fieldSize) {
        final ImmutableList.Builder<FieldSchema> fieldSchemasBuilder = new ImmutableList.Builder<>();
        final FieldSchema targetVariable = new FieldSchema(
                "target",
                0,
                new CategoricalValueSchema(false, targetValues)
        );
        final List<FieldSchema> predictiveFields = IntStream.range(0, fieldSize)
                .mapToObj(index -> new FieldSchema(
                        String.format("field%d", index),
                        index + 1,
                        new NumericValueSchema(false)
                )).collect(Collectors.toList());

        fieldSchemasBuilder.add(targetVariable);
        fieldSchemasBuilder.addAll(predictiveFields);

        return new DatasetSchema(0, fieldSchemasBuilder.build());
    }
}
