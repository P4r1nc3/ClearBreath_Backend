package pl.clearbreath.service;

import pl.clearbreath.dao.response.WeatherForecastResponse;

public interface WeatherService {
    WeatherForecastResponse getWeatherForecast(double lat, double lng);
}
