package ${groupId}.openml;

import com.feedzai.openml.data.Instance;
import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.util.data.ClassificationDatasetSchemaUtil;

import java.nio.file.Path;

public class MyFirstOpenmlModel implements MyOpenmlModel {

    public static final MLAlgorithmDescriptor DESCRIPTOR = null;

    private final DatasetSchema schema;

    public MyFirstOpenmlModel(final DatasetSchema schema) {
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
