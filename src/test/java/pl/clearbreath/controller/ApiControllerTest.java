package pl.clearbreath.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ApiControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiController apiController;

    private String yamlContent;
    private String jsonContent;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        yamlContent = readFileFromResources("openapi/OpenApi.yaml");
        jsonContent = readFileFromResources("openapi/OpenApi.json");
    }

    @Test
    public void testGetOpenApiSuccess() throws IOException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(yamlContent);

        ResponseEntity<String> response = apiController.getOpenApi();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJson = objectMapper.readTree(jsonContent);
        JsonNode actualJson = objectMapper.readTree(response.getBody());

        assertEquals(expectedJson.get("openapi"), actualJson.get("openapi"));
        assertEquals(expectedJson.get("info").get("title"), actualJson.get("info").get("title"));
        assertEquals(expectedJson.get("info").get("version"), actualJson.get("info").get("version"));
    }

    private String readFileFromResources(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)), StandardCharsets.UTF_8);
    }
}
