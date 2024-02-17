package pl.clearbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.WeatherService;

@RestController
@AllArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    @Qualifier("cached-weather-service")
    private final WeatherService weatherService;

    @GetMapping("/lat/{lat}/lng/{lng}")
    public WeatherForecastResponse getMarkerForecast(@PathVariable double lat, @PathVariable double lng) {
        return weatherService.getWeatherForecast(lat, lng);
    }
}
