package com.github.xxdanielngoxx.hui.api.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.shared.config.SecurityConfig;
import com.github.xxdanielngoxx.hui.api.shared.error.RestErrorHandler;
import com.github.xxdanielngoxx.hui.api.user.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.user.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.user.controller.request.RegisterOwnerRequest;
import com.github.xxdanielngoxx.hui.api.user.service.CheckOwnerEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.user.service.CheckOwnerPhoneNumberDuplicatedService;
import com.github.xxdanielngoxx.hui.api.user.service.RegisteringOwnerService;
import com.github.xxdanielngoxx.hui.api.user.service.command.RegisterOwnerCommand;
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

  @MockBean private CheckOwnerPhoneNumberDuplicatedService checkOwnerPhoneNumberDuplicatedService;

  @MockBean private CheckOwnerEmailDuplicatedService checkOwnerEmailDuplicatedService;

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

    @Test
    void
        should_return_an_URI_of_registered_owner_when_registering_with_request_that_contains_all_required_fields()
            throws Exception {
      final UUID mockId = UUID.randomUUID();
      given(registeringOwnerService.register(any(RegisterOwnerCommand.class))).willReturn(mockId);

      final RegisterOwnerRequest request =
          RegisterOwnerRequest.builder()
              .fullName("Ngô Đình Lộc")
              .phoneNumber("0393238017")
              .password("super_secret?#")
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
      assertThat(registerOwnerCommandCaptor.getValue().getEmail()).isNull();
    }
  }

  @Nested
  class CheckPhoneNumberDuplicatedTest {

    @Test
    void should_return_duplicated_is_false_when_owner_phone_number_is_not_duplicated()
        throws Exception {
      final CheckOwnerPhoneNumberDuplicatedRequest request =
          CheckOwnerPhoneNumberDuplicatedRequest.builder().phoneNumber("0393238017").build();

      given(
              checkOwnerPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(
                  request.getPhoneNumber()))
          .willReturn(false);

      mockMvc
          .perform(
              post("/api/v1/owners/actions/check-phone-number-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(false));

      then(checkOwnerPhoneNumberDuplicatedService)
          .should(times(1))
          .checkPhoneNumberDuplicated(request.getPhoneNumber());
    }

    @Test
    void should_return_duplicated_is_true_when_owner_phone_number_is_duplicated() throws Exception {
      final CheckOwnerPhoneNumberDuplicatedRequest request =
          CheckOwnerPhoneNumberDuplicatedRequest.builder().phoneNumber("0393238017").build();

      given(
              checkOwnerPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(
                  request.getPhoneNumber()))
          .willReturn(true);

      mockMvc
          .perform(
              post("/api/v1/owners/actions/check-phone-number-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(true));

      then(checkOwnerPhoneNumberDuplicatedService)
          .should(times(1))
          .checkPhoneNumberDuplicated(request.getPhoneNumber());
    }
  }

  @Nested
  class CheckEmailDuplicatedTest {

    @Test
    void should_return_duplicated_is_false_when_email_is_not_duplicated() throws Exception {
      final CheckOwnerEmailDuplicatedRequest request =
          CheckOwnerEmailDuplicatedRequest.builder().email("danielngo1998@gmail.com").build();

      given(checkOwnerEmailDuplicatedService.checkEmailDuplicated(request.getEmail()))
          .willReturn(false);

      mockMvc
          .perform(
              post("/api/v1/owners/actions/check-email-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.duplicated").value(false));

      then(checkOwnerEmailDuplicatedService)
          .should(times(1))
          .checkEmailDuplicated(request.getEmail());
    }

    @Test
    void should_return_duplicated_is_true_when_email_is_duplicated() throws Exception {
      final CheckOwnerEmailDuplicatedRequest request =
          CheckOwnerEmailDuplicatedRequest.builder().email("danielngo1998@gmail.com").build();

      given(checkOwnerEmailDuplicatedService.checkEmailDuplicated(request.getEmail()))
          .willReturn(true);

      mockMvc
          .perform(
              post("/api/v1/owners/actions/check-email-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(true));

      then(checkOwnerEmailDuplicatedService)
          .should(times(1))
          .checkEmailDuplicated(request.getEmail());
    }
  }
}
