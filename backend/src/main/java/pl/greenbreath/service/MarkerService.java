package pl.greenbreath.service;

import pl.greenbreath.model.Marker;
import pl.greenbreath.model.User;

import java.util.List;


public interface MarkerService {

    List<Marker> getAllMarkers(User user);

    Marker getMarker(double lat, double lng, User user);

    void saveMarker(double lat, double lng, User user);

    void deleteMarker(double lat, double lng, User user);
}
