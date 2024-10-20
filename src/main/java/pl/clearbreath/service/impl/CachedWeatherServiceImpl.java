package pl.clearbreath.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.WeatherService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
final class CachedWeatherServiceImpl implements WeatherService {
    private final WeatherService delegate;
    private final Cache<String, WeatherForecastResponse> cache;

    public CachedWeatherServiceImpl(
            @Qualifier("weather-service") WeatherService delegate,
            MeterRegistry meterRegistry,
            @Value("${weather.cache.expire-after-write-minutes}") long expireAfterWriteMinutes,
            @Value("${weather.cache.max-size}") long maxSize) {

        this.delegate = delegate;

        this.cache = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES)
                .maximumSize(maxSize)
                .build();

        CaffeineCacheMetrics.monitor(meterRegistry, this.cache, "weather-forecast-cache");
    }

    @Override
    public WeatherForecastResponse getWeatherForecast(double lat, double lng) {
        String key = lat + "," + lng;
        return cache.get(key, k -> {
            log.info("Cache miss for location: {}, calling delegate service.", key);
            return delegate.getWeatherForecast(lat, lng);
        });
    }
}
