package pl.clearbreath.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.clearbreath.service.WeatherService;

@Configuration
public class WeatherConfiguration {

    @Primary
    @Bean(name = "cached-weather-service")
    public WeatherService cachedWeatherService(@Qualifier("weather-service") WeatherService weatherService, MeterRegistry meterRegistry) {
        return new CachedWeatherServiceImpl(weatherService, meterRegistry);
    }

    @Bean(name = "weather-service")
    public WeatherService weatherService() {
        return new WeatherServiceImpl();
    }
}

