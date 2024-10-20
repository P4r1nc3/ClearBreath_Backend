package pl.clearbreath;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.response.AirQualityResponse;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
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
        Marker marker = Marker.builder()
                .markerId(1L)
                .lat(52.2297)
                .lng(21.0122)
                .latStation(52.2297)
                .lngStation(21.0122)
                .distance(5.0)
                .continent("Europe")
                .countryName("Poland")
                .city("Warsaw")
                .user(createUser())
                .build();

        return marker;
    }

    public static User createUser() {
        User user = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .password("hashedPassword")
                .role(Role.USER)
                .markers(new HashSet<>())
                .build();

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

    private static <T> T readObjectFromJsonFile(String filePath, Class<T> clazz) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(TestUtils.class.getResourceAsStream(filePath))) {
            return objectMapper.readValue(bufferedInputStream, clazz);
        }
    }
}
