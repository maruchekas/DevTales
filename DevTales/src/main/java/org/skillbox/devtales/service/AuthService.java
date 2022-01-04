package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse getAuthResponse(String userName);
}
