package com.github.blindpirate.gogradle.core.dependency;

import com.github.blindpirate.gogradle.core.dependency.produce.strategy.DependencyProduceStrategy;
import com.github.blindpirate.gogradle.core.dependency.resolve.DependencyResolver;
import org.gradle.api.specs.Spec;

import java.util.Map;
import java.util.Set;

import static com.github.blindpirate.gogradle.core.InjectionHelper.INJECTOR_INSTANCE;
import static com.github.blindpirate.gogradle.core.dependency.produce.strategy.DependencyProduceStrategy.DEFAULT_STRATEGY;

public abstract class AbstractNotationDependency extends AbstractGolangDependency implements NotationDependency {

    public static final String VERSION_KEY = "version";

    private DependencyProduceStrategy strategy = DEFAULT_STRATEGY;

    private ResolvedDependency resolvedDependency;

    @Override
    public DependencyProduceStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(DependencyProduceStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public ResolvedDependency resolve() {
        if (resolvedDependency == null) {
            DependencyResolver resolver = INJECTOR_INSTANCE.getInstance(this.getResolverClass());
            resolvedDependency = resolver.resolve(this);
        }
        return resolvedDependency;
    }

    protected abstract <T extends DependencyResolver> Class<T> getResolverClass();

    public void exclude(Map<String, Object> map) {
        transitiveDepExclusions.add(PropertiesExclusionSpec.of(map));
    }

    public void setTransitive(boolean transitive) {
        if (transitive) {
            transitiveDepExclusions.remove(NO_TRANSITIVE_DEP_SPEC);
        } else {
            transitiveDepExclusions.add(NO_TRANSITIVE_DEP_SPEC);
        }
    }

    @Override
    public Set<Spec<GolangDependency>> getTransitiveDepExclusions() {
        return transitiveDepExclusions;
    }


}
