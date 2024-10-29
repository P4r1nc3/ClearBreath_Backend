package pl.clearbreath.dao.request;

import jakarta.validation.constraints.NotEmpty;
import pl.clearbreath.config.ValidPassword;

public class ChangePasswordRequest {
    @NotEmpty(message = "Old password cannot be empty")
    private String oldPassword;

    @ValidPassword
    @NotEmpty(message = "Password cannot be empty")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
