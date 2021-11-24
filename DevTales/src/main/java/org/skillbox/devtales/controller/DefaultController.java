package org.skillbox.devtales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

  @RequestMapping("/")
  private String index(Model model){
    return "index";
  }

}
