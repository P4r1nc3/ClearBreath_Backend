package pl.clearbreath.service;

import pl.clearbreath.dao.response.AirQualityResponse;

public interface PollutionService {
    AirQualityResponse getPollution(double lat, double lng);
}
