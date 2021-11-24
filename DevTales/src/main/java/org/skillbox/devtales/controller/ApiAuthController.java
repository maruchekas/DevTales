package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.AuthResponse;
import org.skillbox.devtales.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiAuthController {

  private final AuthService authService;

  @GetMapping("/auth/check")
  public ResponseEntity<AuthResponse> auth(){
    return new ResponseEntity<>(authService.result(), HttpStatus.OK);
  }

}
