package ${groupId}.openml;

import com.feedzai.openml.data.schema.DatasetSchema;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.fieldtype.ParamValidationError;
import com.feedzai.openml.provider.exception.ModelLoadingException;
import com.feedzai.openml.provider.model.MachineLearningModelLoader;
import com.feedzai.openml.util.load.LoadSchemaUtils;
import com.feedzai.openml.util.validate.ValidationUtils;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the entity responsible for loading a model from a given directory.
 */
public class MyOpenmlModelLoader implements MachineLearningModelLoader<MyOpenmlModel> {

    private MLAlgorithmDescriptor mlAlgorithmDescriptor;

    public MyOpenmlModelLoader(final MLAlgorithmDescriptor mlAlgorithmDescriptor) {
        this.mlAlgorithmDescriptor = mlAlgorithmDescriptor;
    }

    @Override
    public MyOpenmlModel loadModel(final Path modelPath, final DatasetSchema schema) throws ModelLoadingException {
        if(mlAlgorithmDescriptor.getAlgorithmName().equals(MyFirstOpenmlModel.DESCRIPTOR.getAlgorithmName())) {
            return new MyFirstOpenmlModel(schema);
        }

        if(mlAlgorithmDescriptor.getAlgorithmName().equals(MySecondOpenmlModel.DESCRIPTOR.getAlgorithmName())) {
            return new MySecondOpenmlModel(schema);
        }

        throw new ModelLoadingException(String.format("Unknown model: %s", this.mlAlgorithmDescriptor.getAlgorithmName()));
    }

    @Override
    public List<ParamValidationError> validateForLoad(final Path modelPath,
                                                      final DatasetSchema schema,
                                                      final Map<String, String> params) {

        final ImmutableList.Builder<ParamValidationError> errors = ImmutableList.builder();

        errors.addAll(ValidationUtils.checkParams(this.mlAlgorithmDescriptor, params));
        errors.addAll(ValidationUtils.validateModelInDir(modelPath));
        errors.addAll(ValidationUtils.baseLoadValidations(schema, params));

        ValidationUtils.validateCategoricalSchema(schema).ifPresent(errors::add);

        return errors.build();
    }

    @Override
    public DatasetSchema loadSchema(final Path modelPath) throws ModelLoadingException {
        return LoadSchemaUtils.datasetSchemaFromJson(modelPath);
    }
}
