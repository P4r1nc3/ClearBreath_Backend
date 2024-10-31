package pl.clearbreath.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.clearbreath.Constants;

import java.io.IOException;

@Slf4j
@RestController
public class ApiController {
    @GetMapping("/openapi")
    public ResponseEntity<String> getOpenApi() {
        try {
            String yamlContent = new RestTemplate().getForObject(Constants.OPEN_API_URL, String.class);
            return getOpenApiJson(yamlContent);
        } catch (Exception e) {
            log.error("Error while fetching Swagger definition", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> getOpenApiJson(String yamlContent) {
        try {
            String jsonContent = convertYamlToJson(yamlContent);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(jsonContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error while converting Swagger YAML definition to JSON", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String convertYamlToJson(String yaml) throws IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(yaml, Object.class);

        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }
}
