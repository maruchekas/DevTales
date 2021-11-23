package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.AuthCheckResponse;
import org.skillbox.devtales.service.AuthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiAuthController {

  private final AuthCheckService authCheckService;

  @GetMapping("/auth/check")
  public AuthCheckResponse auth(){
    return authCheckService.result();
  }

}
