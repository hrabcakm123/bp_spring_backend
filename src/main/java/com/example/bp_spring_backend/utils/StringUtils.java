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
}
