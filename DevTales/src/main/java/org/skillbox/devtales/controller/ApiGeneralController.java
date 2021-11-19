package org.skillbox.devtales.controller;

import org.skillbox.devtales.api.response.AuthCheckResponse;
import org.skillbox.devtales.api.response.InitResponse;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.service.AuthCheckService;
import org.skillbox.devtales.service.SettingsService;
import org.skillbox.devtales.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final AuthCheckService authCheckService;
  private final TagService tagService;
  private final SettingsService settingsService;
  private final InitResponse initResponse;

  public ApiGeneralController(AuthCheckService authCheckService,
      TagService tagService,
      SettingsService settingsService,
      InitResponse initResponse) {
    this.authCheckService = authCheckService;
    this.tagService = tagService;
    this.settingsService = settingsService;
    this.initResponse = initResponse;
  }

  @GetMapping("/auth/check")
  private AuthCheckResponse authCheck(){
    return authCheckService.result();
  }

  @GetMapping("/tag")
  private TagResponse tag(){
    return tagService.getTagInfo();
  }

  @GetMapping("/settings")
  private SettingsResponse settings(){
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/init")
  private InitResponse init(){
    return initResponse;
  }

}
