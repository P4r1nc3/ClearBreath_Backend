package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
import pl.clearbreath.exception.InvalidLoginDetailsException;
import pl.clearbreath.exception.UserAlreadyExistsException;
import pl.clearbreath.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthenticationController authenticationController;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private JwtAuthenticationResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        signUpRequest = TestUtils.createSignUpRequest();
        signInRequest = TestUtils.createSignInRequest();
        jwtResponse = TestUtils.createJwtAuthenticationResponse();
    }

    @Test
    public void testSignupSuccess() {
        when(authenticationService.signup(signUpRequest)).thenReturn(jwtResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signup(signUpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponse, response.getBody());
        verify(authenticationService, times(1)).signup(signUpRequest);
    }

    @Test
    public void testSigninSuccess() {
        when(authenticationService.signin(signInRequest)).thenReturn(jwtResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signin(signInRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponse, response.getBody());
        verify(authenticationService, times(1)).signin(signInRequest);
    }

    @Test
    public void testHandleInvalidLoginDetailsException() {
        InvalidLoginDetailsException exception = new InvalidLoginDetailsException("Invalid credentials");

        when(httpServletRequest.getRequestURI()).thenReturn("/auth/signin");

        ResponseEntity<Object> response = authenticationController.handleInvalidLoginDetailsException(exception, httpServletRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationService, never()).signin(any(SignInRequest.class));
    }

    @Test
    public void testHandleUserAlreadyExistsException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        when(httpServletRequest.getRequestURI()).thenReturn("/auth/signup");

        ResponseEntity<Object> response = authenticationController.handleUserAlreadyExistsException(exception, httpServletRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(authenticationService, never()).signup(any(SignUpRequest.class));
    }
}
