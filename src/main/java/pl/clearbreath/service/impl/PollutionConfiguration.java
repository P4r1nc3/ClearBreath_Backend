package pl.clearbreath.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.clearbreath.service.PollutionService;

@Configuration
public class PollutionConfiguration {

    @Primary
    @Bean(name = "cached-pollution-service")
    public PollutionService cachedPollutionService(
            @Qualifier("pollution-service") PollutionService pollutionService,
            MeterRegistry meterRegistry,
            @Value("${pollution.cache.expire-after-write-minutes}") long expireAfterWriteMinutes,
            @Value("${pollution.cache.max-size}") long maxSize) {

        return new CachedPollutionServiceImpl(pollutionService, meterRegistry, expireAfterWriteMinutes, maxSize);
    }

    @Bean(name = "pollution-service")
    public PollutionService pollutionService() {
        return new PollutionServiceImpl();
    }
}
