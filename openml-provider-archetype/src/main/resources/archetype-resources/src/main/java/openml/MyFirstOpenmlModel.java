package ${groupId}.openml;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import com.feedzai.openml.provider.descriptor.fieldtype.BooleanFieldType;
import com.feedzai.openml.provider.descriptor.fieldtype.ChoiceFieldType;
import com.feedzai.openml.provider.descriptor.fieldtype.NumericFieldType;
import com.feedzai.openml.util.data.ClassificationDatasetSchemaUtil;
import com.google.common.collect.ImmutableSet;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * A implementation of a Machine Learning model that implements the Feedzai OpenML API.
 * This dummy example returns the same classification for every scoring instance for the sake of simplicity.
 */
public class MyFirstOpenmlModel implements MyOpenmlModel {

    /**
     * Utility method to generate an URL from a string since {@link URL} constructor throws a checked exception.
     * For the sake of simplicity was not extracted into a proper utility class.
     */
    public static URL genURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static final MLAlgorithmDescriptor DESCRIPTOR = new MLAlgorithmDescriptor(
                "MyFirstOpenmlModel",
                ImmutableSet.of(
                        new ModelParameter(
                                "boolean parameter",
                                "parameter example",
                                "this is an example parameter that won't change the algorithm implementation",
                                true,
                                new BooleanFieldType(true)
                        ),
                        new ModelParameter(
                                "choice parameter",
                                "parameter example",
                                "this is an example parameter that won't change the algorithm implementation",
                                true,
                                new ChoiceFieldType(ImmutableSet.of("option 1", "option 2"), "option 1")
                        ),
                        new ModelParameter(
                                "numeric parameter",
                                "parameter example",
                                "this is an example parameter that won't change the algorithm implementation",
                                true,
                                NumericFieldType.max(15, NumericFieldType.ParameterConfigType.INT, 3)
                        )
                ),
                MachineLearningAlgorithmType.BINARY_CLASSIFICATION,
                genURL("https://github.com/feedzai/feedzai-openml")
            );

    private final DatasetSchema schema;

    public MyFirstOpenmlModel(final DatasetSchema schema) {
        this.schema = schema;
    }

    /**
     * This dummy implementation is not very correct. For start, the sum of probabilities for each class should be
     * 1 (100%), or at least an aproximation. As we can see, it returns 0% for each class (0 is the default value
     * when creating a new java array of doubles). Furthermore, unexpected behaviour on the platform may happen
     * since this method does not return results that match the {@link #classify(Instance)} method.
     */
    @Override
    public double[] getClassDistribution(final Instance instance) {
        final int numClassValues = ClassificationDatasetSchemaUtil.getNumClassValues(this.schema);
        return new double[numClassValues];
    }

    /**
     * As one can see we always return the class 0 as the result of scoring the instance.
     */
    @Override
    public int classify(final Instance instance) {
        return 0;
    }

    @Override
    public boolean save(final Path path, final String s) {
        return false;
    }

    @Override
    public DatasetSchema getSchema() {
        return this.schema;
    }

    @Override
    public void close() throws Exception {

    }
}
