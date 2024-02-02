package pl.clearbreath.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.service.PollutionService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
final class CachedPollutionServiceImpl implements PollutionService {
    private final PollutionService delegate;
    private final Cache<String, AirQualityResponse> cache;

    public CachedPollutionServiceImpl(PollutionService delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        this.cache = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        CaffeineCacheMetrics.monitor(meterRegistry, this.cache, "pollution-response-caching");
    }

    @Override
    public AirQualityResponse getPollution(double lat, double lng) {
        String key = lat + "," + lng;
        return cache.get(key, pollutionKey -> delegate.getPollution(lat, lng));
    }
}


