package pl.clearbreath;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
import pl.clearbreath.dao.response.MarkerInfoResponse;
import pl.clearbreath.dao.response.WeatherForecastResponse;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.Role;
import pl.clearbreath.model.User;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Marker createMarker() {
        Marker marker = new Marker();
        marker.setMarkerId(1L);
        marker.setLat(52.2297);
        marker.setLng(21.0122);
        marker.setLatStation(52.2297);
        marker.setLngStation(21.0122);
        marker.setDistance(5.0);
        marker.setContinent("Europe");
        marker.setCountryName("Poland");
        marker.setCity("Warsaw");
        marker.setUser(createUser());
        marker.setCreatedAt(LocalDateTime.now());

        return marker;
    }

    public static User createUser() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword("hashedPassword");
        user.setRole(Role.USER);
        user.setMarkers(new HashSet<>());

        return user;
    }

    public static ChangePasswordRequest createChangePasswordRequest() {
        return new ChangePasswordRequest("oldPassword123", "newPassword123");
    }

    public static SignUpRequest createSignUpRequest() {
        return new SignUpRequest("John", "Doe", "john@example.com", "password123");
    }

    public static SignInRequest createSignInRequest() {
        return new SignInRequest("john@example.com", "password123");
    }

    public static JwtAuthenticationResponse createJwtAuthenticationResponse() {
        return new JwtAuthenticationResponse("jwt_token");
    }

    @SneakyThrows
    public static AirQualityResponse createAirQualityResponse() {
        return readObjectFromJsonFile("/AirQualityResponse.json", AirQualityResponse.class);
    }

    @SneakyThrows
    public static WeatherForecastResponse createWeatherForecastResponse() {
        return readObjectFromJsonFile("/WeatherForecastResponse.json", WeatherForecastResponse.class);
    }

    @SneakyThrows
    public static MarkerInfoResponse createMarkerInfoResponse() {
        return readObjectFromJsonFile("/MarkerInfoResponse.json", MarkerInfoResponse.class);
    }

    private static <T> T readObjectFromJsonFile(String filePath, Class<T> clazz) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(TestUtils.class.getResourceAsStream(filePath))) {
            return objectMapper.readValue(bufferedInputStream, clazz);
        }
    }
}
