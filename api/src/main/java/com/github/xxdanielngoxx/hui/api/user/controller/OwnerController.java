package com.github.xxdanielngoxx.hui.api.user.controller;

import com.github.xxdanielngoxx.hui.api.shared.error.ApiError;
import com.github.xxdanielngoxx.hui.api.user.controller.mapper.RegisterOwnerMapper;
import com.github.xxdanielngoxx.hui.api.user.controller.request.CheckOwnerEmailDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.user.controller.request.CheckOwnerPhoneNumberDuplicatedRequest;
import com.github.xxdanielngoxx.hui.api.user.controller.request.RegisterOwnerRequest;
import com.github.xxdanielngoxx.hui.api.user.controller.response.CheckOwnerEmailDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.user.controller.response.CheckOwnerPhoneNumberDuplicatedResponse;
import com.github.xxdanielngoxx.hui.api.user.service.CheckOwnerEmailDuplicatedService;
import com.github.xxdanielngoxx.hui.api.user.service.CheckOwnerPhoneNumberDuplicatedService;
import com.github.xxdanielngoxx.hui.api.user.service.RegisteringOwnerService;
import com.github.xxdanielngoxx.hui.api.user.service.command.RegisterOwnerCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/owners")
@Tag(name = "owners")
@RequiredArgsConstructor
public class OwnerController {

  private final RegisteringOwnerService registeringOwnerService;

  private final CheckOwnerPhoneNumberDuplicatedService checkOwnerPhoneNumberDuplicatedService;

  private final CheckOwnerEmailDuplicatedService checkingOwnerEmailNotYetUsed;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Register an owner",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Registering owner successful",
            headers = {
              @Header(
                  name = "Location",
                  schema = @Schema(implementation = String.class),
                  description = "URI to get registered owner")
            }),
        @ApiResponse(
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
      })
  public ResponseEntity<Void> register(@Valid @RequestBody final RegisterOwnerRequest request) {

    final RegisterOwnerCommand command = RegisterOwnerMapper.INSTANCE.requestToCommand(request);
    final UUID createdOwnerId = registeringOwnerService.register(command);

    final URI createdOwnerLocation =
        UriComponentsBuilder.fromPath("/api/v1/owners/{id}").buildAndExpand(createdOwnerId).toUri();

    return ResponseEntity.created(createdOwnerLocation).build();
  }

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
        checkOwnerPhoneNumberDuplicatedService.checkPhoneNumberDuplicated(request.getPhoneNumber());

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
        checkingOwnerEmailNotYetUsed.checkEmailDuplicated(request.getEmail());

    final CheckOwnerEmailDuplicatedResponse responseBody =
        CheckOwnerEmailDuplicatedResponse.builder().duplicated(duplicated).build();

    return ResponseEntity.ok(responseBody);
  }
}
