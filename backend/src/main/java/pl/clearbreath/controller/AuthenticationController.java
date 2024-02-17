package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.clearbreath.ControllerUtils;
import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;
import pl.clearbreath.exception.InvalidLoginDetailsException;
import pl.clearbreath.exception.UserAlreadyExistsException;
import pl.clearbreath.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @ExceptionHandler(InvalidLoginDetailsException.class)
    public ResponseEntity<Object> handleInvalidLoginDetailsException(InvalidLoginDetailsException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                "Check your login credentials and try again.",
                request
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Try using a different email.",
                request
        );
    }
}
