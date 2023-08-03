package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void stringToLongContentNumber() {
        String stringSourceContentNumber = "45asw";
                Pattern pattern = Pattern.compile(".*?(\\d+).*");
        Matcher matcherContentNumber = pattern.matcher(stringSourceContentNumber);
        String number = "-1";
        if (matcherContentNumber.find()) {
            number = matcherContentNumber.group(1);
        }
        Long actual = Long.parseLong(number);
        assertEquals(actual, 45L);
    }
    @Test
    void stringToLongWithoutNumber() {
        String stringSourceContentNumber = "45asw";
        String stringSourceWithoutNumber = "asw";
        Pattern pattern = Pattern.compile(".*?(\\d+).*");

        Matcher matcherWithoutNumber = pattern.matcher(stringSourceWithoutNumber);
        String number = "-1";
        if (matcherWithoutNumber.find()) {
            number = matcherWithoutNumber.group(1);
        }
        Long actual = Long.parseLong(number);
        assertEquals(actual, -1L);
    }
}