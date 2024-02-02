package pl.clearbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;
import pl.clearbreath.service.MarkerService;
import pl.clearbreath.service.PollutionService;
import pl.clearbreath.service.WeatherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@RestController
@AllArgsConstructor
@RequestMapping("/markers")
public class MarkerController {
    private MarkerService markerService;

    @Qualifier("cached-pollution-service")
    private final PollutionService pollutionService;

    @Qualifier("cached-weather-service")
    private final WeatherService weatherService;

    @GetMapping
    public Marker getMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getMarker(lat, lng, user);
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getMarkerData(@RequestParam double lat, @RequestParam double lng) {
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

    @GetMapping("/all")
    public List<Marker> getAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getAllMarkers(user);
    }

    @PostMapping
    public Marker saveMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.saveMarker(lat, lng, user);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteMarker(lat, lng, user);

        return ResponseEntity.noContent().build();
    }
}
