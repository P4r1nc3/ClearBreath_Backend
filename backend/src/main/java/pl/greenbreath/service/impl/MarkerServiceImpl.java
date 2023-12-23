package pl.greenbreath.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.greenbreath.Constants;
import pl.greenbreath.dao.response.AirQualityResponse;
import pl.greenbreath.dao.response.MarkerInfoResponse;
import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;
import pl.greenbreath.repository.MarkerRepository;
import pl.greenbreath.service.CalculationService;
import pl.greenbreath.service.MarkerService;
import pl.greenbreath.service.PollutionService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MarkerServiceImpl implements MarkerService {
    private final MarkerRepository markerRepository;
    private final PollutionService pollutionService;
    private final CalculationService calculationService;

    public List<Marker> getAllMarkers(User user) {
        return markerRepository.findByUser(user);
    }

    public Marker getMarker(double lat, double lng, User user) {
        return markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new EntityNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + ", and user: " + user.getUserId()));
    }

    public Marker saveMarker(double lat, double lng, User user) {
        MarkerInfoResponse markerInfo = getMarkerData(lat, lng);

        AirQualityResponse airQualityResponse = pollutionService.getPollution(lat, lng);
        double latStation = airQualityResponse.getData().getCity().getGeo().get(0);
        double lngStation = airQualityResponse.getData().getCity().getGeo().get(1);
        double distance = calculationService.haversine(lat, lng, latStation, lngStation);

        Marker marker = Marker.builder()
                .lat(lat)
                .lng(lng)
                .latStation(latStation)
                .lngStation(lngStation)
                .distance(distance)
                .user(user)
                .continent(markerInfo.getContinent())
                .countryName(markerInfo.getCountryName())
                .city(markerInfo.getCity())
                .build();
        markerRepository.save(marker);
        return marker;
    }

    public void deleteMarker(double lat, double lng, User user) {
        Marker marker = markerRepository.findByLatAndLngAndUser(lat, lng, user)
                .orElseThrow(() -> new EntityNotFoundException("Marker not found for lat: " + lat + ", lng: " + lng + ", and user: " + user.getUserId()));
        markerRepository.delete(marker);
    }

    private MarkerInfoResponse getMarkerData(double lat, double lng) {
        String apiUrl = Constants.BIG_DATA_CLOUD_API_URL + "/data/reverse-geocode-client?latitude=" + lat + "&longitude=" + lng + "&localityLanguage=en";
        return new RestTemplate().getForObject(apiUrl, MarkerInfoResponse.class);
    }
}
