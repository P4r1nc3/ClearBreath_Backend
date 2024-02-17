package pl.clearbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.service.PollutionService;

@RestController
@AllArgsConstructor
@RequestMapping("/pollution")
public class PollutionController {

    @Qualifier("cached-pollution-service")
    private final PollutionService pollutionService;

    @GetMapping("/lat/{lat}/lng/{lng}")
    public AirQualityResponse getMarker(@PathVariable double lat, @PathVariable double lng) {
        return pollutionService.getPollution(lat, lng);
    }
}
