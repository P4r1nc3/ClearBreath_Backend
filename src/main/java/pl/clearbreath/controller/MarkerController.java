package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.ControllerUtils;
import pl.clearbreath.exception.MarkerAlreadyExistException;
import pl.clearbreath.exception.MarkerNotFoundException;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;
import pl.clearbreath.service.MarkerService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/markers")
public class MarkerController {
    private final MarkerService markerService;

    public MarkerController(MarkerService markerService) {
        this.markerService = markerService;
    }

    @PostMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<Marker> saveMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Marker marker = markerService.saveMarker(lat, lng, user);
        return new ResponseEntity<>(marker, HttpStatus.OK);
    }

    @GetMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<Marker> getMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Marker marker = markerService.getMarker(lat, lng, user);
        return new ResponseEntity<>(marker, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Marker>> getAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Marker> markers = markerService.getAllMarkers(user);
        return new ResponseEntity<>(markers, HttpStatus.OK);
    }

    @DeleteMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<?> deleteMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteMarker(lat, lng, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteAllMarkers(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MarkerNotFoundException.class)
    public ResponseEntity<Object> handleMarkerNotFoundException(MarkerNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }

    @ExceptionHandler(MarkerAlreadyExistException.class)
    public ResponseEntity<Object> handleMarkerAlreadyExist(MarkerAlreadyExistException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Try using a different latitude or longitude.",
                request
        );
    }
}
