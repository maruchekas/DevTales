package org.skillbox.devtales.service.impl;

import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.service.AuthService;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements AuthService {

    public AuthResponse result() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(false);
        return authResponse;
    }

}
