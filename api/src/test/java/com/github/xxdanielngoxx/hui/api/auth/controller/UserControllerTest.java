package com.github.xxdanielngoxx.hui.api.auth.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckPhoneNumberDuplicatedService;
import com.github.xxdanielngoxx.hui.api.shared.config.SecurityConfig;
import com.github.xxdanielngoxx.hui.api.shared.error.RestErrorHandler;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = {UserController.class, RestErrorHandler.class})
class UserControllerTest {

  @MockBean private CheckPhoneNumberDuplicatedService checkPhoneNumberDuplicatedService;

  @MockBean private CheckEmailDuplicatedService checkEmailDuplicatedService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Nested
  class CheckPhoneNumberDuplicatedTest {

    @Test
    void should_return_duplicated_is_false_when_owner_phone_number_is_not_duplicated()
        throws Exception {
      final CheckOwnerPhoneNumberDuplicatedRequest request =
          CheckOwnerPhoneNumberDuplicatedRequest.builder().phoneNumber("0393238017").build();

      given(checkPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(request.getPhoneNumber()))
          .willReturn(false);

      mockMvc
          .perform(
              post("/api/v1/users/actions/check-phone-number-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(false));

      then(checkPhoneNumberDuplicatedService)
          .should(times(1))
          .checkPhoneNumberDuplicated(request.getPhoneNumber());
    }

    @Test
    void should_return_duplicated_is_true_when_owner_phone_number_is_duplicated() throws Exception {
      final CheckOwnerPhoneNumberDuplicatedRequest request =
          CheckOwnerPhoneNumberDuplicatedRequest.builder().phoneNumber("0393238017").build();

      given(checkPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(request.getPhoneNumber()))
          .willReturn(true);

      mockMvc
          .perform(
              post("/api/v1/users/actions/check-phone-number-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(true));

      then(checkPhoneNumberDuplicatedService)
          .should(times(1))
          .checkPhoneNumberDuplicated(request.getPhoneNumber());
    }
  }

  @Nested
  class CheckEmailDuplicatedTest {

    @Test
    void should_return_duplicated_is_false_when_email_is_not_duplicated() throws Exception {
      final CheckOwnerEmailDuplicatedRequest request =
          CheckOwnerEmailDuplicatedRequest.builder().username("danielngo1998@gmail.com").build();

      given(checkEmailDuplicatedService.checkEmailDuplicated(request.getUsername()))
          .willReturn(false);

      mockMvc
          .perform(
              post("/api/v1/users/actions/check-email-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.duplicated").value(false));

      then(checkEmailDuplicatedService)
          .should(times(1))
          .checkEmailDuplicated(request.getUsername());
    }

    @Test
    void should_return_duplicated_is_true_when_email_is_duplicated() throws Exception {
      final CheckOwnerEmailDuplicatedRequest request =
          CheckOwnerEmailDuplicatedRequest.builder().username("danielngo1998@gmail.com").build();

      given(checkEmailDuplicatedService.checkEmailDuplicated(request.getUsername()))
          .willReturn(true);

      mockMvc
          .perform(
              post("/api/v1/users/actions/check-email-duplicated")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.duplicated").value(true));

      then(checkEmailDuplicatedService)
          .should(times(1))
          .checkEmailDuplicated(request.getUsername());
    }
  }
}
