package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.greenbreath.dao.response.WeatherForecastResponse;
import pl.greenbreath.service.impl.WeatherServiceImpl;

@RestController
@AllArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherServiceImpl weatherService;

    @GetMapping
    public WeatherForecastResponse getMarkerForecast(@RequestParam double lat, @RequestParam double lng) {
        return weatherService.getWeatherForecast(lat, lng);
    }
}
