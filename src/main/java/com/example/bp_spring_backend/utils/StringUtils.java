package com.example.bp_spring_backend.utils;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;

@UtilityClass
public class StringUtils {

    public String normalize(String input) {
        if (input == null) {
            return null;
        }

        return Normalizer
                .normalize(input.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public String formatDuration(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (minutes > 0) {
            return minutes + " min " + seconds + " sec";
        } else {
            return seconds + " sec";
        }
    }
}
