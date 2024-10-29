package pl.clearbreath.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.PollutionService;
import pl.clearbreath.service.WeatherService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/environmental")
public class EnvironmentalController {
    @Qualifier("cached-pollution-service")
    private final PollutionService pollutionService;

    @Qualifier("cached-weather-service")
    private final WeatherService weatherService;

    public EnvironmentalController(PollutionService pollutionService, WeatherService weatherService) {
        this.pollutionService = pollutionService;
        this.weatherService = weatherService;
    }

    @GetMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<Map<String, Object>> getMarkerData(@PathVariable double lat, @PathVariable double lng) {
        Map<String, Object> response = fetchMarkerData(lat, lng);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, Object> fetchMarkerData(double lat, double lng) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<AirQualityResponse> airQualityFuture = executorService.submit(() -> pollutionService.getPollution(lat, lng));
        Future<WeatherForecastResponse> weatherForecastFuture = executorService.submit(() -> weatherService.getWeatherForecast(lat, lng));

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("airQuality", airQualityFuture.get());
            response.put("weatherForecast", weatherForecastFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            // 500 Internal Server Error
        } finally {
            executorService.shutdown();
        }

        return response;
    }
}
