package pl.greenbreath.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.greenbreath.Constants;
import pl.greenbreath.dao.response.AirQualityResponse;
import pl.greenbreath.service.PollutionService;

@Slf4j
@Service
public class PollutionServiceImpl implements PollutionService {
    @Value("${token.api}")
    private String apiToken;

    @Override
    public AirQualityResponse getPollution(double lat, double lng) {
        String apiUrl = Constants.API_URL + "/feed/geo:" + lat + ";" + lng + "/?token=" + apiToken;
        log.info("Calling: {}", apiUrl);
        return new RestTemplate().getForObject(apiUrl, AirQualityResponse.class);
    }
}
