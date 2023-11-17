package pl.greenbreath.service;

import pl.greenbreath.dao.request.SignUpRequest;
import pl.greenbreath.dao.request.SignInRequest;
import pl.greenbreath.dao.response.JwtAuthenticationResponse;


public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
