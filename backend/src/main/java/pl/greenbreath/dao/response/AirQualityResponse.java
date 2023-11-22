package pl.greenbreath.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class AirQualityResponse {
    private String status;
    private AirQualityData data;
}

@Data
class AirQualityData {
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
class Attribution {
    private String url;
    private String name;
    private String logo;
}

@Data
class City {
    private List<Double> geo;
    private String name;
    private String url;
    private String location;
}

@Data
class Iaqi {
    private Parameter dew;
    private Parameter h;
    private Parameter p;
    private Parameter pm25;
    private Parameter t;
    private Parameter w;
    private Parameter wg;
}

@Data
class Parameter {
    private Double v;
}

@Data
class Time {
    private String s;
    private String tz;
    private long v;
    private String iso;
}

@Data
class Forecast {
    private DailyForecast daily;
}

@Data
class DailyForecast {
    private List<ParameterData> o3;
    private List<ParameterData> pm10;
    private List<ParameterData> pm25;
}

@Data
class ParameterData {
    private int avg;
    private String day;
    private int max;
    private int min;
}

@Data
class Debug {
    private String sync;
}

