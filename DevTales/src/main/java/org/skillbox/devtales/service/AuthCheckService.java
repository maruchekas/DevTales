package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.AuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {

  public AuthCheckResponse result(){
    AuthCheckResponse authCheckResponse = new AuthCheckResponse();
    authCheckResponse.setResult(false);
    return authCheckResponse;
  }

}
