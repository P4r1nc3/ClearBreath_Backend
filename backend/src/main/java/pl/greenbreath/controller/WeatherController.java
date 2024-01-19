package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.greenbreath.dao.response.WeatherForecastResponse;
import pl.greenbreath.service.WeatherService;

@RestController
@AllArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    @Qualifier("cached-weather-service")
    private final WeatherService weatherService;

    @GetMapping
    public WeatherForecastResponse getMarkerForecast(@RequestParam double lat, @RequestParam double lng) {
        return weatherService.getWeatherForecast(lat, lng);
    }
}
