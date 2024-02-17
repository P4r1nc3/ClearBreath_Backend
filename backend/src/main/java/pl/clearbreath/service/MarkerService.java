package pl.clearbreath.service;

import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;

import java.util.List;


public interface MarkerService {

    List<Marker> getAllMarkers(User user);

    void deleteAllMarkers(User user);

    Marker getMarker(double lat, double lng, User user);

    Marker saveMarker(double lat, double lng, User user);

    void deleteMarker(double lat, double lng, User user);
}
