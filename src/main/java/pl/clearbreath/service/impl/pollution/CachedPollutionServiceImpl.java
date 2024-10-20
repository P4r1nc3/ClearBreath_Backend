package pl.clearbreath.service.impl.pollution;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    public CachedPollutionServiceImpl(
            @Qualifier("pollution-service") PollutionService delegate,
            MeterRegistry meterRegistry,
            @Value("${pollution.cache.expire-after-write-minutes}") long expireAfterWriteMinutes,
            @Value("${pollution.cache.max-size}") long maxSize) {

        this.delegate = delegate;

        this.cache = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES)
                .maximumSize(maxSize)
                .build();

        CaffeineCacheMetrics.monitor(meterRegistry, this.cache, "pollution-response-caching");
    }

    @Override
    public AirQualityResponse getPollution(double lat, double lng) {
        String key = lat + "," + lng;
        return cache.get(key, k -> {
            log.info("Cache miss for key: {}, calling the delegate service", key);
            return delegate.getPollution(lat, lng);
        });
    }
}
