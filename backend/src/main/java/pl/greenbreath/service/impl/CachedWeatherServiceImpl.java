package pl.greenbreath.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.greenbreath.dao.response.WeatherForecastResponse;
import pl.greenbreath.service.WeatherService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CachedWeatherServiceImpl implements WeatherService {
    private final WeatherService delegate;
    private final Cache<String, WeatherForecastResponse> cache;

    public CachedWeatherServiceImpl(WeatherService delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        this.cache = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        CaffeineCacheMetrics.monitor(meterRegistry, this.cache, "pollution-response-caching");
    }
    @Override
    public WeatherForecastResponse getWeatherForecast(double lat, double lng) {
        String key = lat + "," + lng;
        WeatherForecastResponse cachedResponse = cache.getIfPresent(key);

        if (cachedResponse != null) {
            return cachedResponse;
        }

        WeatherForecastResponse response = delegate.getWeatherForecast(lat, lng);
        cache.put(key, response);

        return response;
    }
}
