package pl.clearbreath.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class AirQualityResponse {
    private String status;
    private AirQualityData data;

    @Data
    public static class AirQualityData {
        private int aqi;
        private int idx;
        private List<Attribution> attributions;
        private City city;
        private String dominentpol;
        private Iaqi iaqi;
        private Time time;
        private Forecast forecast;
        private Debug debug;
    }

    @Data
    public static class Attribution {
        private String url;
        private String name;
        private String logo;
    }

    @Data
    public static class City {
        private List<Double> geo;
        private String name;
        private String url;
        private String location;
    }

    @Data
    public static class Iaqi {
        private Parameter dew;
        private Parameter h;
        private Parameter p;
        private Parameter pm25;
        private Parameter t;
        private Parameter w;
        private Parameter wg;
    }

    @Data
    public static class Parameter {
        private Double v;
    }

    @Data
    public static class Time {
        private String s;
        private String tz;
        private long v;
        private String iso;
    }

    @Data
    public static class Forecast {
        private DailyForecast daily;
    }

    @Data
    public static class DailyForecast {
        private List<ParameterData> o3;
        private List<ParameterData> pm10;
        private List<ParameterData> pm25;
        private List<ParameterData> uvi;
    }

    @Data
    public static class ParameterData {
        private int avg;
        private String day;
        private int max;
        private int min;
    }

    @Data
    public static class Debug {
        private String sync;
    }
}

