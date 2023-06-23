package ru.practicum.shareit.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Converter {
    public static long stringToLong(String stringSource) {
        // Шаблон выбирает первое число из строки
        Pattern pattern = Pattern.compile(".*?(\\d+).*");
        Matcher matcher = pattern.matcher(stringSource);
        String number = "-1";
        if (matcher.find()) {
            number = matcher.group(1);
        }
        return Long.parseLong(number);
    }
}
