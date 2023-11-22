package pl.greenbreath.service;

import pl.greenbreath.dao.response.AirQualityResponse;

public interface PollutionService {
    AirQualityResponse getPollution(double lat, double lng);
}
