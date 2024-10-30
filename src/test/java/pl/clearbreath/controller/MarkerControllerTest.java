package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.clearbreath.TestUtils;
import pl.clearbreath.exception.MarkerAlreadyExistException;
import pl.clearbreath.exception.MarkerNotFoundException;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;
import pl.clearbreath.service.MarkerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class MarkerControllerTest {

    @Mock
    private MarkerService markerService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private MarkerController markerController;

    private User testUser;
    private Marker testMarker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = TestUtils.createUser();
        testMarker = TestUtils.createMarker();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testSaveMarker() {
        double lat = testMarker.getLat();
        double lng = testMarker.getLng();
        when(markerService.saveMarker(lat, lng, testUser)).thenReturn(testMarker);

        ResponseEntity<Marker> savedMarker = markerController.saveMarker(lat, lng);

        assertEquals(OK, savedMarker.getStatusCode());
        assertEquals(testMarker, savedMarker.getBody());
        verify(markerService, times(1)).saveMarker(lat, lng, testUser);
    }

    @Test
    public void testGetMarker() {
        double lat = testMarker.getLat();
        double lng = testMarker.getLng();
        when(markerService.getMarker(lat, lng, testUser)).thenReturn(testMarker);

        ResponseEntity<Marker> retrievedMarker = markerController.getMarker(lat, lng);

        assertEquals(OK, retrievedMarker.getStatusCode());
        assertEquals(testMarker, retrievedMarker.getBody());
        verify(markerService, times(1)).getMarker(lat, lng, testUser);
    }

    @Test
    public void testGetAllMarkers() {
        List<Marker> markers = List.of(testMarker);
        when(markerService.getAllMarkers(testUser)).thenReturn(markers);

        ResponseEntity<List<Marker>> retrievedMarkers = markerController.getAllMarkers();

        assertEquals(OK, retrievedMarkers.getStatusCode());
        assertEquals(1, retrievedMarkers.getBody().size());
        assertEquals(testMarker, retrievedMarkers.getBody().get(0));
        verify(markerService, times(1)).getAllMarkers(testUser);
    }

    @Test
    public void testDeleteMarker() {
        double lat = testMarker.getLat();
        double lng = testMarker.getLng();

        ResponseEntity<?> response = markerController.deleteMarker(lat, lng);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(markerService, times(1)).deleteMarker(lat, lng, testUser);
    }

    @Test
    public void testDeleteAllMarkers() {
        ResponseEntity<?> response = markerController.deleteAllMarkers();

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(markerService, times(1)).deleteAllMarkers(testUser);
    }

    @Test
    public void testHandleMarkerNotFoundException() {
        MarkerNotFoundException exception = new MarkerNotFoundException("Marker not found");

        when(httpServletRequest.getRequestURI()).thenReturn("/markers/lat/52.2297/lng/21.0122");

        ResponseEntity<Object> response = markerController.handleMarkerNotFoundException(exception, httpServletRequest);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Marker not found"));
    }

    @Test
    public void testHandleMarkerAlreadyExistException() {
        MarkerAlreadyExistException exception = new MarkerAlreadyExistException("Marker already exists");

        when(httpServletRequest.getRequestURI()).thenReturn("/markers/lat/52.2297/lng/21.0122");

        ResponseEntity<Object> response = markerController.handleMarkerAlreadyExist(exception, httpServletRequest);

        assertEquals(CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Marker already exists"));
    }
}
