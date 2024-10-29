package pl.clearbreath.service.pollution;

import pl.clearbreath.dao.response.AirQualityResponse;

public interface PollutionService {
    AirQualityResponse getPollution(double lat, double lng);
}
