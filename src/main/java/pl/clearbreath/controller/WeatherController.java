package pl.clearbreath.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.weather.WeatherService;

@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Qualifier("cached-weather-service")
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<WeatherForecastResponse> getMarkerForecast(@PathVariable double lat, @PathVariable double lng) {
        WeatherForecastResponse forecast = weatherService.getWeatherForecast(lat, lng);
        return new ResponseEntity<>(forecast, HttpStatus.OK);
    }
}
