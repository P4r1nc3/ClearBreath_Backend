package pl.clearbreath.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.service.pollution.PollutionService;

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
    public ResponseEntity<AirQualityResponse> getMarker(@PathVariable double lat, @PathVariable double lng) {
        AirQualityResponse airQualityResponse = pollutionService.getPollution(lat, lng);
        return new ResponseEntity<>(airQualityResponse, HttpStatus.OK);
    }
}
