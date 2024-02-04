package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.ControllerUtils;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.exception.MarkerNotFoundException;
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

    @GetMapping("/lat/{lat}/lng/{lng}")
    public Marker getMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getMarker(lat, lng, user);
    }

    @GetMapping("/lat/{lat}/lng{lng}/data")
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

    @ExceptionHandler(MarkerNotFoundException.class)
    public ResponseEntity<Object> handleMarkerNotFoundException(MarkerNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }

    @ExceptionHandler(MarkerAlreadyExistException.class)
    public ResponseEntity<Object> handleMarkerAlreadyExist(MarkerAlreadyExistException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Try using a different latitude or longitude.",
                request
        );
    }
}
