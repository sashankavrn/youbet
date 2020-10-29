package com.youbetcha.util;

import java.util.regex.Pattern;

public class InputValidator {

    // Overload to check string isn't empty and return correct keyword
    public static String validateStringInputRequired(String input) {
        return validateStringInput(input, 1, Integer.MAX_VALUE, "", true);
    }

    // Overload to check string doesn't exceed max length and return correct keyword
    public static String validateStringInputWithMaxLength(String input, int maxLength) {
        return validateStringInput(input, 0, maxLength, "", false);
    }

    // Overload to check string has only digits and return correct keyword
    public static String validateStringInputDigitsOnly(String input) {
        return validateStringInput(input, 1, Integer.MAX_VALUE, "^[0-9]+$", true);
    }

    // Overload to check string has length within the defined range, and return correct keyword
    public static String validateStringInputLengthRange(String input, int minLength, int maxLength) {
        return validateStringInput(input, minLength, maxLength, "", true);
    }

    // Overload to check string has only digits and length within the defined range, and return correct keyword
    public static String validateStringInputDigitsOnly(String input, int minLength, int maxLength) {
        return validateStringInput(input, minLength, maxLength, "^[0-9]+$", true);
    }

    // Overload to check string has length within the defined range even if input is not required,
    // and return correct keyword
    public static String validateStringInputLengthRange(String input, int minLength, int maxLength, boolean required) {
        return validateStringInput(input, minLength, maxLength, "", required);
    }

    // Overload to check string has length within the defined range and matches a pattern, and return correct keyword
    public static String validateStringInputWithRegexp(String input, int minLength, int maxLength, String regexp) {
        return validateStringInput(input, minLength, maxLength, regexp, true);
    }

    // Overload to check string has length within the defined range and matches a pattern even if input is not required,
    // and return correct keyword
    public static String validateStringInputWithRegexp(String input, int minLength, int maxLength, String regexp, boolean required) {
        return validateStringInput(input, minLength, maxLength, regexp,  required);
    }

    public static String validateStringInput(String input, int minLength, int maxLength, String regexp, boolean required) {

        if (required && input.length() == 0) return "globalMessages.required";
        if (required && input.length() < minLength || !required && input.length() > 0 && input.length() < minLength)
            return "globalMessages.short";
        if (input.length() > maxLength) return "globalMessages.long";

        if (required && regexp.equals("^[0-9]+$") || !required && input.length() > 0 && regexp.equals("^[0-9]+$")) {
            try {
                Long inputNum = Long.parseLong(input);
            } catch (NumberFormatException e) {
                return "globalMessages.just.numbers.allowed";
            }
        }

        if (required && !regexp.equals("") || !required && input.length() > 0 && !regexp.equals("")) {
            Pattern pattern = Pattern.compile(regexp);
            if (!pattern.matcher(input).matches()) return "globalMessages.invalid";
        }

        return "";
    }
}
