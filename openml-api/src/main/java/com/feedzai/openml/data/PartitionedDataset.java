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

import java.util.function.Predicate;

/**
 * A simple holder for two {@link Dataset}s based on {@link Dataset#partition(Predicate)}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public interface PartitionedDataset {

    /**
     * Gets the {@link Dataset} where all contents matched the partitioning predicated.
     *
     * @return The {@link Dataset}.
     */
    Dataset getMatchedData();

    /**
     * Gets the {@link Dataset} where none of the contents matched the partitioning predicated.
     *
     * @return The {@link Dataset}.
     */
    Dataset getUnmatchedData();

}
