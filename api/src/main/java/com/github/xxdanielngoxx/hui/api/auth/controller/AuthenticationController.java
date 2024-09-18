package com.github.xxdanielngoxx.hui.api.auth.controller;

import static com.github.xxdanielngoxx.hui.api.auth.constant.CookieConstant.FINGERING_COOKIE_NAME;

import com.github.xxdanielngoxx.hui.api.auth.controller.request.LoginWithUsernamePasswordRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.AccessTokenResponse;
import com.github.xxdanielngoxx.hui.api.auth.service.AccessToken;
import com.github.xxdanielngoxx.hui.api.auth.service.LoginWithUsernamePasswordService;
import com.github.xxdanielngoxx.hui.api.shared.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@Tag(name = "auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final LoginWithUsernamePasswordService loginWithUsernamePasswordService;

  @PostMapping(
      value = "/login",
      params = "authenticationMethod=usernamePassword",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Login with username and password",
      responses = {
        @ApiResponse(
            description = "Login success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccessTokenResponse.class))),
        @ApiResponse(
            description = "Login fail",
            responseCode = "401",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
      })
  public ResponseEntity<AccessTokenResponse> loginWithUsernamePassword(
      @RequestBody final LoginWithUsernamePasswordRequest request,
      @RequestParam(value = "authenticationMethod", defaultValue = "usernamePassword")
          final String authenticationMethod) {
    final AccessToken accessToken =
        loginWithUsernamePasswordService.login(request.getUsername(), request.getPassword());

    final AccessTokenResponse response =
        AccessTokenResponse.builder().token(accessToken.token()).build();

    final ResponseCookie fingeringCookie =
        ResponseCookie.from(FINGERING_COOKIE_NAME)
            .value(accessToken.fingering())
            .sameSite(Cookie.SameSite.STRICT.attributeValue())
            .httpOnly(true)
            .secure(true)
            .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, fingeringCookie.toString())
        .body(response);
  }
}
