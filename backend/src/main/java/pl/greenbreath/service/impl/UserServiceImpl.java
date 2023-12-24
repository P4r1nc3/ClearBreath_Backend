package pl.greenbreath.service.impl;

import pl.greenbreath.model.User;
import pl.greenbreath.repository.UserRepository;
import pl.greenbreath.repository.MarkerRepository;
import pl.greenbreath.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        markerRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
