package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.request.RegisterRequest;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.exception.DuplicateUserEmailException;
import org.skillbox.devtales.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface AuthUserService {

    CommonResponse register(RegisterRequest registerRequest) throws DuplicateUserEmailException;

    AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager);

    AuthResponse getAuthResponse(String userName);

    AuthResponse check(Principal principal);

    AuthResponse logout();

    User getUserByEmail(String userName);
}
