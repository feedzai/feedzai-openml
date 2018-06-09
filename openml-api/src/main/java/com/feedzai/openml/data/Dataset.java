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

package com.feedzai.openml.data;

import com.feedzai.openml.data.schema.DatasetSchema;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A dataset is a container of instances that have the same data schema.
 *
 * @author Pedro Rijo (pedro.rijo@feedzai.com)
 * @since 0.1.0
 */
public interface Dataset {

    /**
     * Gets the {@link DatasetSchema} associated with the data held by this Dataset.
     *
     * @return The {@link DatasetSchema}.
     */
    DatasetSchema getSchema();

    /**
     * Gets the data instance associated with the given index.
     *
     * @param index The index of the instance (zero-based).
     * @return The instance representation associated with the given index.
     */
    Instance instance(int index);

    /**
     * Gets the feature values associated with the given index for each instance.
     *
     * @param index The index of the feature (zero-based).
     * @return The feature representation associated with the given index.
     */
    FeatureValues feature(int index);

    /**
     * Gets an iterator for the instances available in the dataset.
     *
     * @return An {@link Iterator} for the instances.
     */
    Iterator<Instance> getInstances();

    /**
     * Yields a new dataset that is a sub-set of the given one containing only the instances that pass the given
     * predicate.
     *
     * @param predicate The predicate to allow an instance to remain in the computed dataset.
     * @return The new dataset.
     */
    Dataset filter(Predicate<Instance> predicate);

    /**
     * Yields a set of new datasets, each of which contains all the instances that yield the same result when passed
     * through the given grouping function.
     *
     * @param function The function used to map each instance to one of the output datasets.
     * @param <K>      The concrete type of the value used for the grouping.
     * @return A map that has each sub-set of this dataset associated with a value for which all of its instances have
     * the same value.
     */
    <K> Map<K, Dataset> groupBy(Function<Instance, K> function);

    /**
     * Segments the given dataset into two.
     *
     * @param predicate The predicate used to split the dataset into two.
     * @return A {@link PartitionedDataset}.
     */
    PartitionedDataset partition(Predicate<Instance> predicate);

    /**
     * Yields an empty dataset with the same schema as this one.
     *
     * @return An empty dataset.
     */
    Dataset empty();
}
