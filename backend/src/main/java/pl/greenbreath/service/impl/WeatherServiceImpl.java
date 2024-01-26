package pl.greenbreath.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.greenbreath.Constants;
import pl.greenbreath.dao.response.WeatherForecastResponse;
import pl.greenbreath.service.WeatherService;

@Slf4j
@Service
final class WeatherServiceImpl implements WeatherService {
    @Value("${token.tomorrow.api}")
    private String apiTomorrowToken;

    @Override
    public WeatherForecastResponse getWeatherForecast(double lat, double lng) {
        String apiUrl = Constants.TOMORROW_IO_API_URL + "/weather/forecast?location=" + lat + "," + lng + "&timesteps=1d&units=metric&apikey=" + apiTomorrowToken;
        log.info("Calling: {}", apiUrl);
        return new RestTemplate().getForObject(apiUrl, WeatherForecastResponse.class);
    }
}
