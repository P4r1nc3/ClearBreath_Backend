package pl.clearbreath.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.MarkerInfoResponse;
import pl.clearbreath.exception.MarkerAlreadyExistException;
import pl.clearbreath.exception.MarkerNotFoundException;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;
import pl.clearbreath.repository.MarkerRepository;
import pl.clearbreath.service.impl.MarkerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MarkerServiceImplTest {

    @Mock
    private MarkerRepository markerRepository;

    @Mock
    private PollutionService pollutionService;

    @Mock
    private CalculationService calculationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MarkerServiceImpl markerService;

    private User testUser;
    private Marker testMarker;
    private AirQualityResponse airQualityResponse;
    private MarkerInfoResponse markerInfoResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = TestUtils.createUser();
        testMarker = TestUtils.createMarker();
        airQualityResponse = TestUtils.createAirQualityResponse();
        markerInfoResponse = TestUtils.createMarkerInfoResponse();
    }

    @Test
    public void testGetAllMarkers() {
        when(markerRepository.findByUser(testUser)).thenReturn(List.of(testMarker));

        var markers = markerService.getAllMarkers(testUser);

        assertEquals(1, markers.size());
        assertEquals(testMarker, markers.get(0));
    }

    @Test
    public void testDeleteAllMarkers() {
        markerService.deleteAllMarkers(testUser);

        verify(markerRepository, times(1)).deleteByUser(testUser);
    }

    @Test
    public void testGetMarker_Success() {
        when(markerRepository.findByLatAndLngAndUser(10, 20, testUser)).thenReturn(Optional.of(testMarker));

        Marker marker = markerService.getMarker(10, 20, testUser);

        assertEquals(testMarker, marker);
    }

    @Test
    public void testGetMarker_NotFound() {
        when(markerRepository.findByLatAndLngAndUser(anyDouble(), anyDouble(), any(User.class)))
                .thenReturn(Optional.empty());

        assertThrows(MarkerNotFoundException.class, () -> markerService.getMarker(10, 20, testUser));
    }

    @Test
    public void testSaveMarker_Success() {
        when(markerRepository.findByLatAndLngAndUser(anyDouble(), anyDouble(), any(User.class)))
                .thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(MarkerInfoResponse.class))).thenReturn(markerInfoResponse);
        when(pollutionService.getPollution(anyDouble(), anyDouble())).thenReturn(airQualityResponse);
        when(calculationService.haversine(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(5.0);

        Marker marker = markerService.saveMarker(10, 20, testUser);

        verify(markerRepository, times(1)).save(any(Marker.class));
        assertEquals("Am Timan", marker.getCity());
        assertEquals("Chad", marker.getCountryName());
    }

    @Test
    public void testSaveMarker_AlreadyExists() {
        when(markerRepository.findByLatAndLngAndUser(anyDouble(), anyDouble(), any(User.class)))
                .thenReturn(Optional.of(testMarker));

        assertThrows(MarkerAlreadyExistException.class, () -> markerService.saveMarker(10, 20, testUser));
        verify(markerRepository, never()).save(any(Marker.class));
    }

    @Test
    public void testDeleteMarker_Success() {
        when(markerRepository.findByLatAndLngAndUser(anyDouble(), anyDouble(), any(User.class)))
                .thenReturn(Optional.of(testMarker));

        markerService.deleteMarker(10, 20, testUser);

        verify(markerRepository, times(1)).delete(testMarker);
    }

    @Test
    public void testDeleteMarker_NotFound() {
        when(markerRepository.findByLatAndLngAndUser(anyDouble(), anyDouble(), any(User.class)))
                .thenReturn(Optional.empty());

        assertThrows(MarkerNotFoundException.class, () -> markerService.deleteMarker(10, 20, testUser));
        verify(markerRepository, never()).delete(any(Marker.class));
    }
}
