package ${groupId}.openml;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.util.data.ClassificationDatasetSchemaUtil;
import com.google.common.collect.ImmutableSet;

import java.net.URL;
import java.nio.file.Path;

/**
 * A Machine Learning model implementation the it's the same as {@link MyFirstOpenmlModel}.
 * This implementation could obviously be different, but for the sake of simplicity
 * no changes have been made. It's only purpose it's to have a provider with
 * more than one algorithm.
 */
public class MySecondOpenmlModel implements MyOpenmlModel {

    public static final MLAlgorithmDescriptor DESCRIPTOR = new MLAlgorithmDescriptor(
            "MySecondOpenmlModel",
            ImmutableSet.of(),
            MachineLearningAlgorithmType.SUPERVISED_BINARY_CLASSIFICATION,
            MyFirstOpenmlModel.genURL("https://github.com/feedzai/feedzai-openml")
    );

    private final DatasetSchema schema;

    public MySecondOpenmlModel(final DatasetSchema schema) {
        this.schema = schema;
    }

    @Override
    public double[] getClassDistribution(final Instance instance) {
        final int numClassValues = ClassificationDatasetSchemaUtil.getNumClassValues(this.schema);
        return new double[numClassValues];
    }

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
