package io.github.rivon0507.optimalassignment.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncSolverConfig {
    @Bean
    public Executor solverJobExecutor(@Value("${assignmentsolver.threadpool.queue-capacity}") int queueCapacity,
                                      @Value("${assignmentsolver.threadpool.max-pool-size}") int maxPoolSize,
                                      @Value("${assignmentsolver.threadpool.core-pool-size}") int corePoolSize
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("ASolver-");
        executor.initialize();
        return executor;
    }
}
