package pl.clearbreath.dao.response;

import lombok.Data;
import java.util.List;

@Data
public class WeatherForecastResponse {
    private Timelines timelines;
    private Location location;

    @Data
    public static class Timelines {
        private List<DailyTimeline> daily;
    }

    @Data
    public static class DailyTimeline {
        private String time;
        private Values values;
    }

    @Data
    public static class Values {
        private double cloudBaseAvg;
        private double cloudBaseMax;
        private double cloudBaseMin;
        private double cloudCeilingAvg;
        private double cloudCeilingMax;
        private double cloudCeilingMin;
        private double cloudCoverAvg;
        private double cloudCoverMax;
        private double cloudCoverMin;
        private double dewPointAvg;
        private double dewPointMax;
        private double dewPointMin;
        private double evapotranspirationAvg;
        private double evapotranspirationMax;
        private double evapotranspirationMin;
        private double evapotranspirationSum;
        private double freezingRainIntensityAvg;
        private double freezingRainIntensityMax;
        private double freezingRainIntensityMin;
        private double humidityAvg;
        private double humidityMax;
        private double humidityMin;
        private double iceAccumulationAvg;
        private double iceAccumulationLweAvg;
        private double iceAccumulationLweMax;
        private double iceAccumulationLweMin;
        private double iceAccumulationLweSum;
        private double iceAccumulationMax;
        private double iceAccumulationMin;
        private double iceAccumulationSum;
        private String moonriseTime;
        private String moonsetTime;
        private double precipitationProbabilityAvg;
        private double precipitationProbabilityMax;
        private double precipitationProbabilityMin;
        private double pressureSurfaceLevelAvg;
        private double pressureSurfaceLevelMax;
        private double pressureSurfaceLevelMin;
        private double rainAccumulationAvg;
        private double rainAccumulationLweAvg;
        private double rainAccumulationLweMax;
        private double rainAccumulationLweMin;
        private double rainAccumulationMax;
        private double rainAccumulationMin;
        private double rainAccumulationSum;
        private double rainIntensityAvg;
        private double rainIntensityMax;
        private double rainIntensityMin;
        private double sleetAccumulationAvg;
        private double sleetAccumulationLweAvg;
        private double sleetAccumulationLweMax;
        private double sleetAccumulationLweMin;
        private double sleetAccumulationLweSum;
        private double sleetAccumulationMax;
        private double sleetAccumulationMin;
        private double sleetIntensityAvg;
        private double sleetIntensityMax;
        private double sleetIntensityMin;
        private double snowAccumulationAvg;
        private double snowAccumulationLweAvg;
        private double snowAccumulationLweMax;
        private double snowAccumulationLweMin;
        private double snowAccumulationLweSum;
        private double snowAccumulationMax;
        private double snowAccumulationMin;
        private double snowAccumulationSum;
        private double snowDepthAvg;
        private double snowDepthMax;
        private double snowDepthMin;
        private double snowDepthSum;
        private double snowIntensityAvg;
        private double snowIntensityMax;
        private double snowIntensityMin;
        private String sunriseTime;
        private String sunsetTime;
        private double temperatureApparentAvg;
        private double temperatureApparentMax;
        private double temperatureApparentMin;
        private double temperatureAvg;
        private double temperatureMax;
        private double temperatureMin;
        private double uvHealthConcernAvg;
        private double uvHealthConcernMax;
        private double uvHealthConcernMin;
        private double uvIndexAvg;
        private double uvIndexMax;
        private double uvIndexMin;
        private double visibilityAvg;
        private double visibilityMax;
        private double visibilityMin;
        private int weatherCodeMax;
        private int weatherCodeMin;
        private double windDirectionAvg;
        private double windGustAvg;
        private double windGustMax;
        private double windGustMin;
        private double windSpeedAvg;
        private double windSpeedMax;
        private double windSpeedMin;
    }

    @Data
    public static class Location {
        private double lat;
        private double lon;
    }
}
