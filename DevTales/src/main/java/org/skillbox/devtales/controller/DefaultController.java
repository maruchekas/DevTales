package org.skillbox.devtales.controller;

import org.skillbox.devtales.api.response.InitResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

  private final InitResponse initResponse;

  public DefaultController(InitResponse initResponse) {
    this.initResponse = initResponse;
  }

  @RequestMapping("/")
  private String index(Model model){
  System.out.println(initResponse.getTitle());
  System.out.println(initResponse.getSubtitle());
  System.out.println(initResponse.getPhone());
  System.out.println(initResponse.getEmail());
  System.out.println(initResponse.getCopyright());
  System.out.println(initResponse.getCopyrightFrom());
    return "index";
  }

}
