package pl.greenbreath.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class MarkerInfoResponse {
    private double latitude;
    private double longitude;
    private String lookupSource;
    private String localityLanguageRequested;
    private String continent;
    private String continentCode;
    private String countryName;
    private String countryCode;
    private String principalSubdivision;
    private String principalSubdivisionCode;
    private String city;
    private String locality;
    private String postcode;
    private String plusCode;
    private LocalityInfo localityInfo;

    @Data
    public static class LocalityInfo {
        private List<AdministrativeInfo> administrative;
        private List<InformativeInfo> informative;
    }

    @Data
    public static class AdministrativeInfo {
        private String name;
        private String description;
        private String isoName;
        private int order;
        private int adminLevel;
        private String isoCode;
        private String wikidataId;
        private int geonameId;
    }

    @Data
    public static class InformativeInfo {
        private String name;
        private String description;
        private String isoName;
        private int order;
        private String isoCode;
        private String wikidataId;
    }
}

