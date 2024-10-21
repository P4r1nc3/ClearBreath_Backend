package pl.clearbreath.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
import pl.clearbreath.exception.InvalidLoginDetailsException;
import pl.clearbreath.exception.UserAlreadyExistsException;
import pl.clearbreath.model.User;
import pl.clearbreath.repository.UserRepository;
import pl.clearbreath.service.JwtService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private JwtAuthenticationResponse jwtResponse;
    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signUpRequest = TestUtils.createSignUpRequest();
        signInRequest = TestUtils.createSignInRequest();
        jwtResponse = TestUtils.createJwtAuthenticationResponse();
        testUser = TestUtils.createUser();
    }

    @Test
    public void testSignup_Success() {
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtResponse.getToken());

        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        assertNotNull(response);
        assertEquals(jwtResponse.getToken(), response.getToken());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignup_UserAlreadyExists() {
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signup(signUpRequest));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testSignin_Success() {
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn(jwtResponse.getToken());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        JwtAuthenticationResponse response = authenticationService.signin(signInRequest);

        assertNotNull(response);
        assertEquals(jwtResponse.getToken(), response.getToken());
    }

    @Test
    public void testSignin_InvalidLoginDetails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InvalidLoginDetailsException("Invalid login details provided."));

        assertThrows(InvalidLoginDetailsException.class, () -> authenticationService.signin(signInRequest));
    }
}
