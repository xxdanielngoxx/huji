package com.github.xxdanielngoxx.hui.api.shared.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.xxdanielngoxx.hui.api.shared.time.Instants;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiError {

  @Builder.Default private UUID errorId = UUID.randomUUID();

  @JsonFormat(pattern = Instants.ISO_DATE_TIME_FORMAT, timezone = Instants.TIMEZONE)
  @Builder.Default
  private Instant instant = Instant.now();

  private HttpStatus status;

  private List<String> errors;

  private String path;
}
