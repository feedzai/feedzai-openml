package ${groupId}.openml;

import com.feedzai.openml.provider.MachineLearningProvider;
import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;
import java.util.Set;

/**
 * This is your new custom OpenML provider. As such, it implements
 * {@link MachineLearningProvider}. This simple example only provides
 * a Loading Provider. If you want to enable model training directly from
 * Feedzai platform, you should change (or create a new class) {@link MyOpenmlModelLoader}
 * to support training. Check the class javadocs for more details.
 */
@AutoService(MachineLearningProvider.class)
public class MyOpenmlProvider implements MachineLearningProvider<MyOpenmlModelLoader> {

    private static final String NAME = "My OpenML Provider";

    public String getName() {
        return NAME;
    }

    /**
     * Several strategies could be employed here. This archetype uses the simplest one: It contains
     * a manually curated list of Algorithms. A safer solution should not require manual intervention
     * on this list after adding new algorithms. This can be achieved by using a Java Enum for instance.
     */
    public Set<MLAlgorithmDescriptor> getAlgorithms() {
        return ImmutableSet.of(MyFirstOpenmlModel.DESCRIPTOR, MySecondOpenmlModel.DESCRIPTOR);
    }

    /**
     * Since our MyOpenmlModelLoader implementation is quite simple and generic for all our
     * algorithms, this method is very simple: it just looks for an algorithm with the given name
     * and creates a MyOpenmlModelLoader with the name. For more complex use cases (different loaders
     * for different algorithms) the implementation may not be as trivial.
     */
    public Optional<MyOpenmlModelLoader> getModelCreator(final String algorithmName) {
        return getAlgorithms()
                .stream()
                .filter(mlAlgorithmDescriptor -> mlAlgorithmDescriptor.getAlgorithmName().equals(algorithmName))
                .findFirst()
                .map(MyOpenmlModelLoader::new);
    }
}
