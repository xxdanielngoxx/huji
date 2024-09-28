package com.github.xxdanielngoxx.hui.api.auth.controller;

import static com.github.xxdanielngoxx.hui.api.shared.springdoc.OpenAPIConfig.BEARER_SECURITY_SCHEMA_KEY;

import com.github.xxdanielngoxx.hui.api.auth.controller.mapper.GetCurrentUserMapper;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.CheckOwnerEmailDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.CheckOwnerPhoneNumberDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.UserResource;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenAuthenticatedPrincipal;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckPhoneNumberDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.GetUserByUsernameService;
import com.github.xxdanielngoxx.hui.api.shared.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/users")
@Tag(name = "users")
@RequiredArgsConstructor
public class UserController {

  private final CheckPhoneNumberDuplicatedService checkPhoneNumberDuplicatedService;

  private final CheckEmailDuplicatedService checkEmailDuplicatedService;

  private final GetUserByUsernameService getUserByUsernameService;

  @PostMapping(
      value = "/actions/check-phone-number-duplicated",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Check whether the phone number is duplicated",
      responses = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(implementation = CheckOwnerPhoneNumberDuplicatedResponse.class))),
      })
  public ResponseEntity<CheckOwnerPhoneNumberDuplicatedResponse> checkPhoneNumberDuplicated(
      @RequestBody final CheckOwnerPhoneNumberDuplicatedRequest request) {
    final boolean duplicated =
        checkPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(request.getPhoneNumber());

    final CheckOwnerPhoneNumberDuplicatedResponse responseBody =
        CheckOwnerPhoneNumberDuplicatedResponse.builder().duplicated(duplicated).build();

    return ResponseEntity.ok(responseBody);
  }

  @PostMapping(
      value = "/actions/check-email-duplicated",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Check whether the email is duplicated",
      responses = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CheckOwnerEmailDuplicatedResponse.class))),
      })
  public ResponseEntity<CheckOwnerEmailDuplicatedResponse> checkEmailNotYetUsed(
      @RequestBody final CheckOwnerEmailDuplicatedRequest request) {
    final boolean duplicated =
        checkEmailDuplicatedService.checkEmailDuplicated(request.getUsername());

    final CheckOwnerEmailDuplicatedResponse responseBody =
        CheckOwnerEmailDuplicatedResponse.builder().duplicated(duplicated).build();

    return ResponseEntity.ok(responseBody);
  }

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get the current user information",
      responses = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResource.class))),
        @ApiResponse(
            responseCode = "403",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
      })
  @SecurityRequirement(name = BEARER_SECURITY_SCHEMA_KEY)
  public ResponseEntity<UserResource> getCurrentUser(
      @AuthenticationPrincipal final AccessTokenAuthenticatedPrincipal principal) {
    final UserEntity userEntity = getUserByUsernameService.getUserByUsername(principal.getName());

    final UserResource userResource = GetCurrentUserMapper.INSTANCE.entityToResource(userEntity);

    return ResponseEntity.ok(userResource);
  }
}
