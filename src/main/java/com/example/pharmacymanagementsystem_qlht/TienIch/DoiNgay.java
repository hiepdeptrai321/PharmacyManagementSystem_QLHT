package com.example.pharmacymanagementsystem_qlht.TienIch;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DoiNgay {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DoiNgay() {}

    public static String dinhDangNgay(Date d) {
        if (d == null) return "";
        LocalDate ld = d.toLocalDate();
        return ld.format(DATE_FORMATTER);
    }

    public static String dinhDangThoiGian(Timestamp ts) {
        if (ts == null) return "";
        Instant instant = ts.toInstant();
        LocalDate ld = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return ld.format(DATE_FORMATTER);
    }
}

