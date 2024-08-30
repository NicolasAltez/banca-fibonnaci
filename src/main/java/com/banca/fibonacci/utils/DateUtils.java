package com.banca.fibonacci.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private DateUtils() {
    }

    public static String formatCurrentDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

