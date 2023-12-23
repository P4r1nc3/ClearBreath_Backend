package pl.greenbreath.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.greenbreath.service.PollutionService;

@Configuration
public class PollutionConfiguration {

    @Primary
    @Bean(name = "cached-pollution-service")
    public PollutionService cachedPollutionService(@Qualifier("pollution-service") PollutionService pollutionService, MeterRegistry meterRegistry) {
        return new CachedPollutionServiceImpl(pollutionService, meterRegistry);
    }

    @Bean(name = "pollution-service")
    public PollutionService pollutionService() {
        return new PollutionServiceImpl();
    }
}
