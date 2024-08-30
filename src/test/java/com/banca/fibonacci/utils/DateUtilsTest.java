package com.banca.fibonacci.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void formatCurrentDateTime_ShouldReturnFormattedDateTimeCorrectly() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        String expectedFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String actualFormat = DateUtils.formatCurrentDateTime();

        assertEquals(expectedFormat, actualFormat);
    }

}