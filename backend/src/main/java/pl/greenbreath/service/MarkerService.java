package pl.greenbreath.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;
import pl.greenbreath.repository.MarkerRepository;
import pl.greenbreath.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MarkerService {
    private MarkerRepository markerRepository;

    public List<Marker> getAllMarkers(User user) {
        return markerRepository.findByUser(user);
    }

    public Marker getMarker(double lat, double lng, User user) {
        return markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new EntityNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + ", and user: " + user.getUserId()));
    }

    public void saveMarker(double lat, double lng, User user) {
        Marker marker = Marker.builder()
                .lat(lat)
                .lng(lng)
                .user(user)
                .build();
        markerRepository.save(marker);
    }

    public void deleteMarker(double lat, double lng, User user) {
        Marker marker = markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new EntityNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + ", and user: " + user.getUserId()));
        markerRepository.delete(marker);
    }
}
