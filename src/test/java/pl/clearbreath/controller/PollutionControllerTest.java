package pl.clearbreath.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.service.pollution.PollutionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

public class PollutionControllerTest {

    @Mock
    private PollutionService pollutionService;

    @InjectMocks
    private PollutionController pollutionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMarker() {
        double lat = 52.2297;
        double lng = 21.0122;

        AirQualityResponse mockResponse = TestUtils.createAirQualityResponse();

        when(pollutionService.getPollution(lat, lng)).thenReturn(mockResponse);

        ResponseEntity<AirQualityResponse> response = pollutionController.getMarker(lat, lng);

        assertEquals(OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(pollutionService, times(1)).getPollution(lat, lng);
    }
}
