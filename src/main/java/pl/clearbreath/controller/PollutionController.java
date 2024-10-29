package pl.clearbreath.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.service.PollutionService;

@Slf4j
@RestController
@RequestMapping("/pollution")
public class PollutionController {
    @Qualifier("cached-pollution-service")
    private final PollutionService pollutionService;

    public PollutionController(PollutionService pollutionService) {
        this.pollutionService = pollutionService;
    }

    @GetMapping("/lat/{lat}/lng/{lng}")
    public AirQualityResponse getMarker(@PathVariable double lat, @PathVariable double lng) {
        return pollutionService.getPollution(lat, lng);
    }
}
