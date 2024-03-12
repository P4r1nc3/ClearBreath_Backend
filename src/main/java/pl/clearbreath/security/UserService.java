package pl.clearbreath.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.model.User;

public interface UserService {
    UserDetailsService userDetailsService();
    void deleteUser(User user);
    void changePassword(User user, ChangePasswordRequest changePasswordRequest);
}
