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

package com.feedzai.util.data.schema;

import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.data.schema.NumericValueSchema;
import com.feedzai.openml.data.schema.StringValueSchema;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Helper class to build {@link DatasetSchema}s to use in tests.
 *
 * @author Henrique Costa (henrique.costa@feedzai.com)
 * @since 0.1.0
 */
public final class TestDatasetSchemaBuilder {

    /**
     * The default nominal values to set in all categorical fields.
     */
    public static final ImmutableSet<String> DEFAULT_NOMINAL_VALUES = ImmutableSet.of("false", "true");

    /**
     * Whether the target variable should be categorical or numeric.
     */
    private boolean categoricalTarget = true;

    /**
     * The nominal values to set in all the categorical fields.
     */
    private Set<String> nominalValues = DEFAULT_NOMINAL_VALUES;

    /**
     * The number of numerical fields to create.
     */
    private int numericals = 0;

    /**
     * The number of categorical fields to create after the numericals.
     */
    private int categoricals = 0;

    /**
     * The number of string fields to create after the categoricals.
     */
    private int strings = 0;

    /**
     * Private constructor, use {@link #builder()} instead.
     */
    private TestDatasetSchemaBuilder() {
    }

    /**
     * Creates a new builder with the default values.
     *
     * @return The blank builder.
     */
    public static TestDatasetSchemaBuilder builder() {
        return new TestDatasetSchemaBuilder();
    }

    /**
     * Creates a {@link DatasetSchema} according to the configurations done to this builder.
     * <p>
     * The target variable of the resulting schema will be the first of the categorical fields (default), or the first
     * of the numerical fields if there are no categoricals in the schema or if {@link #withNumericalTarget()} was
     * called.
     *
     * @return The resulting {@link DatasetSchema} with numeric fields followed by categoricals followed by strings.
     */
    public DatasetSchema build() {
        final ImmutableList.Builder<FieldSchema> fieldBuilder = ImmutableList.builder();
        final int totalFields = this.numericals + this.categoricals + this.strings;

        for (int schemaFieldIndex = 0; schemaFieldIndex < totalFields; schemaFieldIndex++) {
            final AbstractValueSchema valueSchema;
            if (schemaFieldIndex < this.numericals) {
                valueSchema = new NumericValueSchema(true);
            } else if (schemaFieldIndex < this.numericals + this.categoricals) {
                valueSchema = new CategoricalValueSchema(true, this.nominalValues);
            } else {
                valueSchema = new StringValueSchema(true);
            }

            fieldBuilder.add(
                    new FieldSchema(
                            "field" + schemaFieldIndex,
                            schemaFieldIndex,
                            valueSchema
                    )
            );
        }

        return new DatasetSchema(
                // The first categorical comes after the numericals
                this.categoricalTarget && this.categoricals > 0 ? this.numericals : 0,
                fieldBuilder.build()
        );
    }

    /**
     * Defines how many numerical fields the schema should include.
     *
     * @param numericals The number of numerical fields.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withNumericalFields(final int numericals) {
        this.numericals = numericals;
        return this;
    }

    /**
     * Configures the end {@link DatasetSchema} to point to the first numerical field as target variable.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withNumericalTarget() {
        return withCategoricalTarget(false);
    }

    /**
     * Defines how many categorical fields the schema should include.
     *
     * @param categoricals The number of categorical fields.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withCategoricalFields(final int categoricals) {
        return withCategoricalFields(categoricals, DEFAULT_NOMINAL_VALUES);
    }

    /**
     * Defines how many categorical fields the schema should include and the nominal values that they should have.
     *
     * @param categoricals  The number of categorical fields.
     * @param nominalValues The Set of nominal values that will be configured in all categoricals.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withCategoricalFields(final int categoricals, final Set<String> nominalValues) {
        this.categoricals = categoricals;
        this.nominalValues = nominalValues;
        return this;
    }

    /**
     * Configures the end {@link DatasetSchema} to point to the first categorical field as target variable.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withCategoricalTarget() {
        return withCategoricalTarget(true);
    }

    /**
     * Configures the end {@link DatasetSchema} to have a target according to the given parameter.
     *
     * @param categoricalTarget {true} if the resulting schema should have a categorical target variable, {@code false}
     *                          otherwise.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withCategoricalTarget(final boolean categoricalTarget) {
        this.categoricalTarget = categoricalTarget;
        return this;
    }

    /**
     * Defines how many string fields the schema should include.
     *
     * @param strings The number of string fields.
     * @return {@code this} instance.
     */
    public TestDatasetSchemaBuilder withStringFields(final int strings) {
        this.strings = strings;
        return this;
    }
}
