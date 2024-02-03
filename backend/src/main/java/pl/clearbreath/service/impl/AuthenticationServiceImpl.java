package pl.clearbreath.service.impl;

import pl.clearbreath.exception.InvalidLoginDetailsException;
import pl.clearbreath.exception.UserAlreadyExistsException;
import pl.clearbreath.model.Role;
import pl.clearbreath.model.User;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
import pl.clearbreath.repository.UserRepository;
import pl.clearbreath.service.AuthenticationService;
import pl.clearbreath.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Such user already exists.");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new InvalidLoginDetailsException("Invalid login details provided.");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidLoginDetailsException("Invalid login details provided."));

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
