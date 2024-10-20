package pl.clearbreath.service.impl.weather;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.clearbreath.service.WeatherService;

@Configuration
public class WeatherConfiguration {

    @Primary
    @Bean(name = "cached-weather-service")
    public WeatherService cachedWeatherService(
            @Qualifier("weather-service") WeatherService weatherService,
            MeterRegistry meterRegistry,
            @Value("${weather.cache.expire-after-write-minutes}") long expireAfterWriteMinutes,
            @Value("${weather.cache.max-size}") long maxSize) {
        return new CachedWeatherServiceImpl(weatherService, meterRegistry, expireAfterWriteMinutes, maxSize);
    }

    @Bean(name = "weather-service")
    public WeatherService weatherService() {
        return new WeatherServiceImpl();
    }
}

