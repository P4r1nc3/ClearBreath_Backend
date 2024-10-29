package pl.clearbreath.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public WeatherForecastResponse getMarkerForecast(@PathVariable double lat, @PathVariable double lng) {
        return weatherService.getWeatherForecast(lat, lng);
    }
}
