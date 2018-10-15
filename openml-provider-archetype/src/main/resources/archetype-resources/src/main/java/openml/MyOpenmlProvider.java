package ${groupId}.openml;

import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;
import java.util.Set;

@AutoService(MachineLearningProvider.class)
public class MyOpenmlProvider implements MachineLearningProvider<MyOpenmlModelLoader> {

    public static final String NAME = "My OpenML Provider";

    public String getName() {
        return NAME;
    }

    public Set<MLAlgorithmDescriptor> getAlgorithms() {
        return ImmutableSet.of(MyFirstOpenmlModel.DESCRIPTOR, MySecondOpenmlModel.DESCRIPTOR);
    }

    public Optional<MyOpenmlModelLoader> getModelCreator(final String algorithmName) {
        return getAlgorithms()
                .stream()
                .filter(mlAlgorithmDescriptor -> mlAlgorithmDescriptor.getAlgorithmName().equals(algorithmName))
                .findFirst()
                .map(MyOpenmlModelLoader::new);
    }
}
