package com.github.xxdanielngoxx.hui.api.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth")
@RequiredArgsConstructor
public class AuthController {

  @PostMapping("login")
  public ResponseEntity<Void> login() {
    return ResponseEntity.accepted().build();
  }
}
