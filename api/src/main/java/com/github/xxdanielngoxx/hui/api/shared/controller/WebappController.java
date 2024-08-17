package com.github.xxdanielngoxx.hui.api.shared.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebappController {

  @GetMapping(value = {"/", "/webapp"})
  public String index() {
    return "forward:/webapp/index.html";
  }
}
