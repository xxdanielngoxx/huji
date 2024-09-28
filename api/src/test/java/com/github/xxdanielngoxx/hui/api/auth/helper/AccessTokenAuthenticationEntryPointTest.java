package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.shared.error.ApiError;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class AccessTokenAuthenticationEntryPointTest {

  @InjectMocks private AccessTokenAuthenticationEntryPoint accessTokenAuthenticationEntryPoint;

  @Mock private ObjectMapper objectMapper;

  @Test
  void should_return_status_401() throws ServletException, IOException {
    final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    final MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    given(objectMapper.writeValueAsString(any(ApiError.class))).willReturn("error message");

    accessTokenAuthenticationEntryPoint.commence(
        mockRequest, mockResponse, new BadCredentialsException("Invalid bearer token"));

    assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    then(objectMapper).should(times(1)).writeValueAsString(any(ApiError.class));
  }
}
