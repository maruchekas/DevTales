package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.InitResponse;
import org.skillbox.devtales.api.response.SettingsResponse;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.service.SettingsService;
import org.skillbox.devtales.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final InitResponse initResponse;
  private final SettingsService settingsService;
  private final TagService tagService;

  @GetMapping("/init")
  private InitResponse init(){
    return initResponse;
  }

  @GetMapping("/settings")
  private ResponseEntity<SettingsResponse> settings(){
    return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  private ResponseEntity<TagResponse> tag(){
    return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
  }

}
