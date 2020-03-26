/*
 * Copyright 2020 Feedzai
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

package com.feedzai.openml.util.data.csv;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.AbstractValueSchema;
import com.feedzai.openml.data.schema.CategoricalValueSchema;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.data.schema.FieldSchema;
import com.feedzai.openml.mocks.MockDataset;
import com.feedzai.openml.mocks.MockInstance;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * Class to aid reading CSV's and transforming
 * data to the OpenML Instance/Dataset formats.
 *
 * Useful to load test data from CSV files with
 * an arbitrary schema (so one file can be used
 * for multiple test purposes with ease).
 *
 * @author Alberto Ferreira (alberto.ferreira@feedzai.com)
 * @since TODO
 */
public class CSVUtils {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(CSVUtils.class);

    /**
     * @return CSVParser for a csv file with header.
     * @throws IOException in case there's an IO error.
     */
    public static CSVParser getCSVParser(final Path csvFilePath) throws IOException {

        final File csvFile = new File(csvFilePath.toString());
        final FileInputStream csvFileInputStream = new FileInputStream(csvFile);
        return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new InputStreamReader(csvFileInputStream));
    }

    /**
     * Takes the schema and from an arbitrary CSV record returns a new instance with the values (doubles) assigned.
     *
     * @param schema The scoring schema
     * @param record An arbitrary input CSVRecord
     * @return MockInstance containing the values (doubles) of the features according to the schema.
     */
    public static MockInstance createDoublesInstanceFromCSVRecord(final DatasetSchema schema,
                                                                  final CSVRecord record) {

        final List<FieldSchema> fields = schema.getFieldSchemas();
        final int numFields = fields.size();

        final double[] fieldValues = new double[numFields];
        for (int fieldIdx = 0; fieldIdx < numFields; ++fieldIdx) {
            final FieldSchema fieldSchema = fields.get(fieldIdx);
            final String fieldName = fieldSchema.getFieldName();
            final AbstractValueSchema fieldValueSchema = fieldSchema.getValueSchema();

            final String rawRecordValue = record.get(fieldName);

            if (fieldValueSchema instanceof CategoricalValueSchema) {
                fieldValues[fieldIdx] = rawCategoricalValueToIndex(
                        (CategoricalValueSchema) fieldValueSchema,
                        rawRecordValue
                );
            } else {
                fieldValues[fieldIdx] = Double.parseDouble(rawRecordValue);
            }
        }

        return new MockInstance(fieldValues);
    }

    /**
     * Converts a raw value into the index of the categorical feature according to the schema.
     *
     * @param categoricalFieldSchema Schema of the categorical feature.
     * @param value                  Raw input value
     * @return Converted index of the category.
     */
    public static int rawCategoricalValueToIndex(final CategoricalValueSchema categoricalFieldSchema,
                                                 final String value) {

        final SortedSet<String> sortedCategoricalValues = categoricalFieldSchema.getNominalValues();
        if (!categoricalFieldSchema.validate(value)) {
            logger.error("Received invalid input value {} for field with values {}.", value, sortedCategoricalValues);
            throw new RuntimeException("Invalid input value for categorical field!");
        }

        final Iterator<String> iterator = sortedCategoricalValues.iterator();
        int categoryIndex = 0;
        while (iterator.hasNext()) {
            final String category = iterator.next();

            if (category.equals(value)) {
                return categoryIndex;
            }
            ++categoryIndex;
        }

        throw new RuntimeException("Invalid input value for categorical field!");
    }

    /**
     * Gets the csv data into a dataset with the desired schema
     *
     * @param csvFilepath Path to the CSV file with header.
     * @param schema      Schema with desired columns to extract from the csv.
     * @return Dataset with the instances according to the desired schema.
     * @throws IOException in case reading the CSV fails.
     */
    public static MockDataset getDatasetWithSchema(final Path csvFilepath,
                                                   final DatasetSchema schema) throws IOException {

        final CSVParser csvParser = getCSVParser(csvFilepath);

        final List<Instance> instances = new ArrayList<>();
        for (final CSVRecord record : csvParser) {
            instances.add(
                    createDoublesInstanceFromCSVRecord(schema, record)
            );
        }

        return new MockDataset(schema, instances);
    }

    /**
     * Gets the first rows from the CSV data into a dataset with the desired schema
     *
     * @param csvFilepath     Path to the CSV file with header.
     * @param schema          Schema with desired columns to extract from the csv.
     * @param maxNumInstances Maximum number of instances to retrieve from the CSV.
     * @return Dataset with the instances according to the desired schema.
     * @throws IOException in case reading the CSV fails.
     */
    public static MockDataset getDatasetWithSchema(final Path csvFilepath,
                                                   final DatasetSchema schema,
                                                   final int maxNumInstances) throws IOException {

        final CSVParser csvParser = getCSVParser(csvFilepath);

        final List<Instance> instances = new ArrayList<>();
        final Iterator<CSVRecord> csvRecordIterator = csvParser.iterator();
        for (int numInstances = 0; numInstances < maxNumInstances && csvRecordIterator.hasNext(); ++numInstances) {
            instances.add(
                    createDoublesInstanceFromCSVRecord(schema, csvRecordIterator.next())
            );
        }

        return new MockDataset(schema, instances);
    }
}
