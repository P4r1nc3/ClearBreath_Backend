package pl.clearbreath.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.clearbreath.Constants;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.MarkerInfoResponse;
import pl.clearbreath.exception.MarkerAlreadyExistException;
import pl.clearbreath.exception.MarkerNotFoundException;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;
import pl.clearbreath.repository.MarkerRepository;
import pl.clearbreath.service.pollution.PollutionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MarkerService {
    private final MarkerRepository markerRepository;

    @Qualifier("cached-pollution-service")
    private final PollutionService pollutionService;

    private final CalculationService calculationService;

    public MarkerService(MarkerRepository markerRepository,
                         PollutionService pollutionService,
                         CalculationService calculationService) {
        this.markerRepository = markerRepository;
        this.pollutionService = pollutionService;
        this.calculationService = calculationService;
    }

    public List<Marker> getAllMarkers(User user) {
        return markerRepository.findByUser(user);
    }

    @Transactional
    public void deleteAllMarkers(User user) {
        markerRepository.deleteByUser(user);
    }

    public Marker getMarker(double lat, double lng, User user) {
        return markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new MarkerNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + "."));
    }

    public Marker saveMarker(double lat, double lng, User user) {
        Optional<Marker> existingMarker = markerRepository.findByLatAndLngAndUser(lat, lng, user);
        if (existingMarker.isPresent()) {
            throw new MarkerAlreadyExistException("A marker already exists at the specified lat: " + lat + " and lng: " + lng + ".");
        }

        MarkerInfoResponse markerInfo = getMarkerData(lat, lng);

        AirQualityResponse airQualityResponse = pollutionService.getPollution(lat, lng);
        double latStation = airQualityResponse.getData().getCity().getGeo().get(0);
        double lngStation = airQualityResponse.getData().getCity().getGeo().get(1);
        double distance = calculationService.haversine(lat, lng, latStation, lngStation);

        Marker marker = new Marker();
        marker.setCreatedAt(LocalDateTime.now());
        marker.setLat(lat);
        marker.setLng(lng);
        marker.setLatStation(latStation);
        marker.setLngStation(lngStation);
        marker.setDistance(distance);
        marker.setUser(user);
        marker.setContinent(markerInfo.getContinent());
        marker.setCountryName(markerInfo.getCountryName());
        marker.setCity(markerInfo.getCity());

        markerRepository.save(marker);
        return marker;
    }

    public void deleteMarker(double lat, double lng, User user) {
        Marker marker = markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new MarkerNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + "."));
        markerRepository.delete(marker);
    }

    private MarkerInfoResponse getMarkerData(double lat, double lng) {
        String apiUrl = Constants.BIG_DATA_CLOUD_API_URL + "/data/reverse-geocode-client?latitude=" + lat + "&longitude=" + lng + "&localityLanguage=en";
        log.info("Calling: {}", apiUrl);
        return new RestTemplate().getForObject(apiUrl, MarkerInfoResponse.class);
    }
}
