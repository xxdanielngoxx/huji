package com.github.xxdanielngoxx.hui.api.auth.controller;

import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.CheckOwnerEmailDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.auth.controller.response.CheckOwnerPhoneNumberDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.auth.service.CheckPhoneNumberDuplicatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/users")
@Tag(name = "users")
@RequiredArgsConstructor
public class UserController {

  private final CheckPhoneNumberDuplicatedService checkPhoneNumberDuplicated;

  private final CheckEmailDuplicatedService checkEmailDuplicated;

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
        checkPhoneNumberDuplicated.checkPhoneNumberDuplicated(request.getPhoneNumber());

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
    final boolean duplicated = checkEmailDuplicated.checkEmailDuplicated(request.getUsername());

    final CheckOwnerEmailDuplicatedResponse responseBody =
        CheckOwnerEmailDuplicatedResponse.builder().duplicated(duplicated).build();

    return ResponseEntity.ok(responseBody);
  }
}
