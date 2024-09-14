package com.github.xxdanielngoxx.hui.api.owner.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.owner.controller.request.RegisterOwnerRequest;
import com.github.xxdanielngoxx.hui.api.owner.service.RegisteringOwnerService;
import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import com.github.xxdanielngoxx.hui.api.shared.config.SecurityConfig;
import com.github.xxdanielngoxx.hui.api.shared.error.RestErrorHandler;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = {OwnerController.class, RestErrorHandler.class})
class OwnerControllerTest {

  @MockBean private RegisteringOwnerService registeringOwnerService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Nested
  class RegisterOwnerTest {
    @Test
    void should_return_an_URI_of_registered_owner_when_registering_with_valid_request()
        throws Exception {
      final UUID mockId = UUID.randomUUID();
      given(registeringOwnerService.register(any(RegisterOwnerCommand.class))).willReturn(mockId);

      final RegisterOwnerRequest request =
          RegisterOwnerRequest.builder()
              .fullName("Ngô Đình Lộc")
              .phoneNumber("0393238017")
              .password("super_secret?#")
              .email("danielngo1998@gmail.com")
              .build();

      final MvcResult result =
          mockMvc
              .perform(
                  post("/api/v1/owners")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andReturn();

      assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());

      final String expectedLocation = String.format("/api/v1/owners/%s", mockId);
      assertThat(result.getResponse().getHeader(HttpHeaders.LOCATION)).isEqualTo(expectedLocation);

      final ArgumentCaptor<RegisterOwnerCommand> registerOwnerCommandCaptor =
          ArgumentCaptor.forClass(RegisterOwnerCommand.class);
      then(registeringOwnerService).should(times(1)).register(registerOwnerCommandCaptor.capture());
      assertThat(registerOwnerCommandCaptor.getValue().getFullName())
          .isEqualTo(request.getFullName());
      assertThat(registerOwnerCommandCaptor.getValue().getPhoneNumber())
          .isEqualTo(request.getPhoneNumber());
      assertThat(registerOwnerCommandCaptor.getValue().getPassword())
          .isEqualTo(request.getPassword());
      assertThat(registerOwnerCommandCaptor.getValue().getEmail()).isEqualTo(request.getEmail());
    }
  }
}
