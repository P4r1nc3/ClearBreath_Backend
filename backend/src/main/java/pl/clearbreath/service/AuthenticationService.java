package pl.clearbreath.service;

import pl.clearbreath.dao.request.SignUpRequest;
import pl.clearbreath.dao.request.SignInRequest;
import pl.clearbreath.dao.response.JwtAuthenticationResponse;


public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
