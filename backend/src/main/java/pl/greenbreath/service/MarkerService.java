package pl.greenbreath.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.greenbreath.model.Marker;
import pl.greenbreath.repository.MarkerRepository;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MarkerService {
    private MarkerRepository markerRepository;

    public void saveMarker(double lat, double lng) {
        log.info("Saving marker with coordinates: Lat: " + lat + " Lng: " + lng);
        Marker marker = Marker.builder()
                .lat(lat)
                .lng(lng)
                .build();
        markerRepository.save(marker);
    }

    public void deleteMarker(double lat, double lng) {
        Optional<Marker> markerToDelete = markerRepository.findByLatAndLng(lat, lng);
        log.info("Deleting marker with coordinates: Lat: " + lat + " Lng: " + lng);

        if (markerToDelete.isPresent()) {
            markerRepository.delete(markerToDelete.get());
        } else {
            throw new IllegalArgumentException("Marker not found for coordinates: Lat=" + lat + ", Lng=" + lng);
        }
    }
}
