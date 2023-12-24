package pl.greenbreath.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.greenbreath.model.User;

public interface UserService {
    UserDetailsService userDetailsService();
    void deleteUser(User user);
    void changePassword(User user, String oldPassword, String newPassword);
}
