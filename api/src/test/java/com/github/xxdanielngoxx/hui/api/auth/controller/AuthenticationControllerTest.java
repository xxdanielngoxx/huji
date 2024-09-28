package com.github.xxdanielngoxx.hui.api.auth.controller;

import static com.github.xxdanielngoxx.hui.api.auth.constant.AuthenticationMethodConstant.AUTHENTICATION_METHOD_QUERY_PARAM_NAME;
import static com.github.xxdanielngoxx.hui.api.auth.constant.AuthenticationMethodConstant.USERNAME_PASSWORD_AUTHENTICATION_METHOD_VALUE;
import static com.github.xxdanielngoxx.hui.api.auth.constant.CookieConstant.FINGERING_COOKIE_NAME;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.LoginWithUsernamePasswordRequest;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessToken;
import com.github.xxdanielngoxx.hui.api.auth.service.LoginWithUsernamePasswordService;
import com.github.xxdanielngoxx.hui.api.shared.config.SecurityConfig;
import com.github.xxdanielngoxx.hui.api.shared.error.RestErrorHandler;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = {AuthenticationController.class, RestErrorHandler.class})
class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private LoginWithUsernamePasswordService loginWithUsernamePasswordService;

  @Nested
  class LoginWithUsernamePasswordTest {

    @Test
    void should_return_access_token_when_username_and_password_are_correct() throws Exception {
      final LoginWithUsernamePasswordRequest request =
          LoginWithUsernamePasswordRequest.builder()
              .username("danielngo1998@gmail.com")
              .password("<<redacted>>")
              .build();

      final AccessToken mockAccessToken =
          AccessToken.builder().token("<<token>>").fingering("<<fingering>>").build();

      given(loginWithUsernamePasswordService.login(request.getUsername(), request.getPassword()))
          .willReturn(mockAccessToken);

      mockMvc
          .perform(
              post("/api/v1/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .queryParam(
                      AUTHENTICATION_METHOD_QUERY_PARAM_NAME,
                      USERNAME_PASSWORD_AUTHENTICATION_METHOD_VALUE)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.token").value(mockAccessToken.token()))
          .andExpect(cookie().value(FINGERING_COOKIE_NAME, mockAccessToken.fingering()))
          .andExpect(
              cookie().sameSite(FINGERING_COOKIE_NAME, Cookie.SameSite.STRICT.attributeValue()))
          .andExpect(cookie().path(FINGERING_COOKIE_NAME, "/api/v1"))
          .andExpect(cookie().httpOnly(FINGERING_COOKIE_NAME, true))
          .andExpect(cookie().secure(FINGERING_COOKIE_NAME, true));

      then(loginWithUsernamePasswordService)
          .should(times(1))
          .login(request.getUsername(), request.getPassword());
    }

    @Test
    void should_return_status_401_when_username_or_password_is_incorrect() throws Exception {
      final LoginWithUsernamePasswordRequest request =
          LoginWithUsernamePasswordRequest.builder()
              .username("danielngo1998@gmail.com")
              .password("<<redacted>>")
              .build();

      final BadCredentialsException exception =
          new BadCredentialsException("username or password is incorrect");
      given(loginWithUsernamePasswordService.login(request.getUsername(), request.getPassword()))
          .willThrow(exception);

      mockMvc
          .perform(
              post("/api/v1/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .queryParam(
                      AUTHENTICATION_METHOD_QUERY_PARAM_NAME,
                      USERNAME_PASSWORD_AUTHENTICATION_METHOD_VALUE)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isUnauthorized())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));

      then(loginWithUsernamePasswordService)
          .should(times(1))
          .login(request.getUsername(), request.getPassword());
    }
  }
}
