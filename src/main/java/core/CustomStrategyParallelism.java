package core;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

import static core.EnvProperties.getThreadCount;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

public class CustomStrategyParallelism implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {
    private static final int KEEP_ALIVE_SECONDS = 30;
    private final int threadSize = getThreadCount();

    @Override
    public int getParallelism() {
        return threadSize;
    }

    @Override
    public int getMinimumRunnable() {
        return INTEGER_ZERO;
    }

    @Override
    public int getMaxPoolSize() {
        return threadSize;
    }

    @Override
    public int getCorePoolSize() {
        return threadSize;
    }

    @Override
    public int getKeepAliveSeconds() {
        return KEEP_ALIVE_SECONDS;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
        return this;
    }
}
