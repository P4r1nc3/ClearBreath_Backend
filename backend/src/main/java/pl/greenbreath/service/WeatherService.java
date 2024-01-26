package pl.greenbreath.service;

import pl.greenbreath.dao.response.WeatherForecastResponse;

public interface WeatherService {
    public WeatherForecastResponse getWeatherForecast(double lat, double lng);
}
