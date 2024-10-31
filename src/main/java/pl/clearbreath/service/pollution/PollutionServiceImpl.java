package pl.clearbreath.service.pollution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.clearbreath.Constants;
import pl.clearbreath.dao.response.AirQualityResponse;

@Slf4j
@Service
final class PollutionServiceImpl implements PollutionService {
    @Value("${token.waqi.api}")
    private String apiWaqiToken;

    @Override
    public AirQualityResponse getPollution(double lat, double lng) {
        String apiUrl = Constants.WAQI_API_URL + "/feed/geo:" + lat + ";" + lng + "/?token=" + apiWaqiToken;
        log.info("Calling: {}", apiUrl);
        return new RestTemplate().getForObject(apiUrl, AirQualityResponse.class);
    }
}
