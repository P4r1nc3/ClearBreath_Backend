package pl.greenbreath;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.greenbreath.dao.response.ErrorResponse;

import java.time.Instant;

public final class ControllerUtils {
    // 403 Forbidden
    public static final String FORBIDDEN_CAUSE = "You do not have permission to access this resource.";
    public static final String FORBIDDEN_ACTION = "Check if your account has the necessary permissions.";

    // 404 Not Found
    public static final String NOT_FOUND_CAUSE = "The requested resource was not found.";
    public static final String NOT_FOUND_ACTION = "Verify the request path and try again.";

    // Utility class
    private ControllerUtils() {}

    public static ResponseEntity<Object> createErrorResponse(HttpStatus status, String path, String cause, String action) {
        String timestamp = Instant.now().toString();

        ErrorResponse response = new ErrorResponse(status.value(), status.getReasonPhrase(), path, timestamp, cause, action);

        return new ResponseEntity<>(response, status);
    }

    public static String requestUri(HttpServletRequest request) {
        StringBuilder fullPath = new StringBuilder(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            fullPath.append("?").append(queryString);
        }

        return fullPath.toString();
    }
}
