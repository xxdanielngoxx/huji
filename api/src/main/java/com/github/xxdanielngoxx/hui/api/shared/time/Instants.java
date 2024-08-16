package com.github.xxdanielngoxx.hui.api.shared.time;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Instants {
  private Instants() {}

  public static final String TIMEZONE = "UTC";

  public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

  public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  public static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT).withZone(ZoneOffset.UTC);
}
