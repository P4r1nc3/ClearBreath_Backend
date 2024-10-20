package pl.clearbreath.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.WeatherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMarker() {
        double lat = 10;
        double lng = 20;

        WeatherForecastResponse mockResponse = TestUtils.createWeatherForecastResponse();

        when(weatherService.getWeatherForecast(lat, lng)).thenReturn(mockResponse);

        WeatherForecastResponse response = weatherController.getMarkerForecast(lat, lng);

        assertEquals(mockResponse, response);
        verify(weatherService, times(1)).getWeatherForecast(lat, lng);
    }
}
