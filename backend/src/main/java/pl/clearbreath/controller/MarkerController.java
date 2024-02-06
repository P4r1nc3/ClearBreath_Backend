package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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

@RestController
@AllArgsConstructor
@RequestMapping("/markers")
public class MarkerController {
    private MarkerService markerService;

    @PostMapping("/lat/{lat}/lng/{lng}")
    public Marker saveMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.saveMarker(lat, lng, user);
    }

    @GetMapping("/lat/{lat}/lng/{lng}")
    public Marker getMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getMarker(lat, lng, user);
    }

    @GetMapping
    public List<Marker> getAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getAllMarkers(user);
    }

    @DeleteMapping("/lat/{lat}/lng/{lng}")
    public ResponseEntity<Void> deleteMarker(@PathVariable double lat, @PathVariable double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteMarker(lat, lng, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteAllMarkers(user);
        return ResponseEntity.noContent().build();
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
