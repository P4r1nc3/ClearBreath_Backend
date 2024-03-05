package pl.clearbreath.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.clearbreath.ControllerUtils;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ResponseEntity<Object> responseEntity = ControllerUtils.createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ControllerUtils.UNAUTHORIZED_CAUSE,
                ControllerUtils.UNAUTHORIZED_ACTION,
                request);

        String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}