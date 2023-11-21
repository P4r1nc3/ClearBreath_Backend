package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;
import pl.greenbreath.service.MarkerService;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/markers")
public class MarkerController {
    private MarkerService markerService;

    @GetMapping
    public Marker getMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getMarker(lat, lng, user);
    }

    @GetMapping("/all")
    public List<Marker> getAllMarkers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return markerService.getAllMarkers(user);
    }

    @PostMapping
    public String saveMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.saveMarker(lat, lng, user);
        return "Point saved successfully!";
    }

    @DeleteMapping
    public String deleteMarker(@RequestParam double lat, @RequestParam double lng) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        markerService.deleteMarker(lat, lng, user);
        return "Point deleted successfully!";
    }
}
