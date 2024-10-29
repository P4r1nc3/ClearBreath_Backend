package pl.clearbreath.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.exception.InvalidPasswordException;
import pl.clearbreath.exception.SamePasswordException;
import pl.clearbreath.exception.UserNotFoundException;
import pl.clearbreath.model.User;
import pl.clearbreath.repository.UserRepository;
import pl.clearbreath.repository.MarkerRepository;
import pl.clearbreath.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;
    private final ApplicationContext applicationContext;

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + user.getEmail() + " not found"));
        markerRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(User user, ChangePasswordRequest changePasswordRequest) {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if (oldPassword.equals(newPassword)) {
            throw new SamePasswordException("New password must be different from the old password.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Invalid old password.");
        }

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + user.getEmail() + " not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
