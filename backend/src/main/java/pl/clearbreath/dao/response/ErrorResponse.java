package pl.greenbreath.dao.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@JsonInclude(NON_NULL)
public class ErrorResponse {
    private int status;
    private String error;
    private String path;
    private String timestamp;
    private String cause;
    private String action;
}
