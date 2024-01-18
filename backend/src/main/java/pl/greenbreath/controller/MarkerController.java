package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.greenbreath.dao.response.AirQualityResponse;
import pl.greenbreath.dao.response.WeatherForecastResponse;
import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;
import pl.greenbreath.service.MarkerService;
import pl.greenbreath.service.PollutionService;
import pl.greenbreath.service.WeatherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
        CompletableFuture<AirQualityResponse> airQualityFuture = CompletableFuture.supplyAsync(() -> {
            return pollutionService.getPollution(lat, lng);
        });

        CompletableFuture<WeatherForecastResponse> weatherForecastFuture = CompletableFuture.supplyAsync(() -> {
            return weatherService.getWeatherForecast(lat, lng);
        });

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(airQualityFuture, weatherForecastFuture);

        Map<String, Object> response = new HashMap<>();
        try {
            combinedFuture.get();
            response.put("airQuality", airQualityFuture.get());
            response.put("weatherForecast", weatherForecastFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public String deleteMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteMarker(lat, lng, user);
        return "Point deleted successfully!";
    }
}
