package pl.clearbreath.dao.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import pl.clearbreath.config.ValidPassword;
public class SignUpRequest {
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @ValidPassword
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
