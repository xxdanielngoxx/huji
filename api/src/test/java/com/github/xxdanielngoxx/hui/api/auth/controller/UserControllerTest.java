package com.github.xxdanielngoxx.hui.api.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenAuthenticatedPrincipal;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenAuthenticatedToken;
import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckPhoneNumberDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.GetUserByUsernameService;
import com.github.xxdanielngoxx.hui.api.shared.config.SecurityConfig;
import com.github.xxdanielngoxx.hui.api.shared.error.RestErrorHandler;
import java.util.UUID;
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

  @MockBean private GetUserByUsernameService getUserByUsernameService;

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

  @Nested
  class GetCurrentUserTest {
    @Test
    void should_return_user_resource_when_access_token_is_valid() throws Exception {
      final UUID userId = UUID.randomUUID();
      final String username = "danielngo1998@gmail.com";
      final String phoneNumber = "0393238017";
      final Role role = Role.OWNER;

      final UserEntity mockUserEntity =
          UserEntity.builder()
              .id(userId)
              .username(username)
              .phoneNumber(phoneNumber)
              .role(role)
              .build();
      given(getUserByUsernameService.getUserByUsername(username)).willReturn(mockUserEntity);

      final AccessTokenAuthenticatedToken mockAuthenticatedToken =
          new AccessTokenAuthenticatedToken(
              new AccessTokenAuthenticatedPrincipal(username, role), new Object());

      mockMvc
          .perform(get("/api/v1/users/me").with(authentication(mockAuthenticatedToken)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value(userId.toString()))
          .andExpect(jsonPath("$.username").value(username))
          .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
          .andExpect(jsonPath("$.role").value(role.name()));

      then(getUserByUsernameService).should(times(1)).getUserByUsername(username);
    }

    @Test
    void should_return_status_forbidden_when_user_is_anonymous() throws Exception {
      mockMvc.perform(get("/api/v1/users/me").with(anonymous())).andExpect(status().isForbidden());
    }
  }
}
