package pl.clearbreath.dao.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.clearbreath.config.ValidPassword;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotEmpty(message = "Old password cannot be empty")
    private String oldPassword;

    @ValidPassword
    @NotEmpty(message = "Password cannot be empty")
    private String newPassword;
}
