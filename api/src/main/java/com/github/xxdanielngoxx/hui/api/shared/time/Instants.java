package com.github.xxdanielngoxx.hui.api.shared.time;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public interface Instants {
  String TIMEZONE = "UTC";
  ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
  String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT).withZone(ZoneOffset.UTC);
}
