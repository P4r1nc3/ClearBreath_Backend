package pl.clearbreath.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.PollutionService;
import pl.clearbreath.service.WeatherService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EnvironmentalControllerTest {

    @Mock
    private PollutionService pollutionService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private EnvironmentalController environmentalController;

    private AirQualityResponse testAirQualityResponse;
    private WeatherForecastResponse testWeatherForecastResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testAirQualityResponse = TestUtils.createAirQualityResponse();
        testWeatherForecastResponse = TestUtils.createWeatherForecastResponse();
    }

    @Test
    @SneakyThrows
    public void testGetMarkerData() {
        double lat = 52.2297;
        double lng = 21.0122;

        when(pollutionService.getPollution(lat, lng)).thenReturn(testAirQualityResponse);
        when(weatherService.getWeatherForecast(lat, lng)).thenReturn(testWeatherForecastResponse);

        ResponseEntity<Map<String, Object>> response = environmentalController.getMarkerData(lat, lng);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testAirQualityResponse, response.getBody().get("airQuality"));
        assertEquals(testWeatherForecastResponse, response.getBody().get("weatherForecast"));

        verify(pollutionService, times(1)).getPollution(lat, lng);
        verify(weatherService, times(1)).getWeatherForecast(lat, lng);
    }
}
