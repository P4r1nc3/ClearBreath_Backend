package pl.clearbreath.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.service.weather.WeatherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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

        ResponseEntity<WeatherForecastResponse> response = weatherController.getMarkerForecast(lat, lng);

        assertEquals(OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(weatherService, times(1)).getWeatherForecast(lat, lng);
    }
}
