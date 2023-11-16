package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.greenbreath.model.Marker;
import pl.greenbreath.service.MarkerService;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/marker")
@CrossOrigin(origins = "http://localhost:63343")
public class MarkerController {
    private MarkerService markerService;

    @GetMapping("/all")
    public List<Marker> getAllMarkers() {
        return markerService.getAllMarkers();
    }

    @PostMapping()
    public String saveMarker(@RequestParam double lat, @RequestParam double lng) {
        markerService.saveMarker(lat, lng);
        return "Point saved successfully!";
    }

    @DeleteMapping()
    public String deleteMarker(@RequestParam double lat, @RequestParam double lng) {
        markerService.deleteMarker(lat, lng);
        return "Point deleted successfully!";
    }
}
