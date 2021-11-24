package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  public AuthResponse result(){
    AuthResponse authResponse = new AuthResponse();
    authResponse.setResult(false);
    return authResponse;
  }

}
