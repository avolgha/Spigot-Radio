package dev.marius.radio;

import org.jetbrains.annotations.*;

import java.util.regex.Pattern;

public final class FrequencyHelper {
    public static final Pattern frequencyPattern = Pattern.compile("^\\d{1,3}(\\.\\d{1,2})?$");
    public static final Pattern wildcardPattern = Pattern.compile("^\\d{1,3}\\.(\\d{1,2}|x)$");

    private FrequencyHelper() {
    }

    // we have to "parse" the frequency to eliminate doubled channels:
    // 01 -> 1
    public static @NotNull String parseFrequency(String frequency) {
        return String.format("%.2f", Float.parseFloat(frequency)).replace(',', '.');
    }

    public static boolean validateFrequency(@NotNull String frequency) {
        return frequency.matches(frequencyPattern.pattern());
    }
}
