package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.greenbreath.dao.response.AirQualityResponse;
import pl.greenbreath.service.PollutionService;

@RestController
@AllArgsConstructor
@RequestMapping("/pollution")
public class PollutionController {
    private PollutionService pollutionService;

    @GetMapping
    public AirQualityResponse getMarker(@RequestParam double lat, @RequestParam double lng) {
        return pollutionService.getPollution(lat, lng);
    }
}
