package servlets;

import java.time.ZoneOffset;

public class ParseUTC {
    public static String parseUTC(String notFormatedUtc) {
        String raw = notFormatedUtc.replaceAll("(?i)(UTC|GMT)", "").replaceAll("\\s+", "");

        if (!raw.startsWith("+") && !raw.startsWith("-")) {
            raw = "+" + raw;
        }

        if (raw.matches("[+-]\\d{2}:\\d{2}")) {
            return ZoneOffset.of(raw).toString();
        }

        if (raw.matches("[+-]?\\d{1,2}")) {
            int hours = Integer.parseInt(raw);
            return ZoneOffset.ofHours(hours).toString();
        }

        if (raw.matches("[+-]?\\d{3,4}")) {
            int hours = Integer.parseInt(raw.substring(0, raw.length() - 2));
            int minutes = Integer.parseInt(raw.substring(raw.length() - 2));
            return ZoneOffset.ofHoursMinutes(hours, minutes).toString();
        }
        throw new IllegalArgumentException("Invalid format for ZoneOffset: " + raw);
    }
}