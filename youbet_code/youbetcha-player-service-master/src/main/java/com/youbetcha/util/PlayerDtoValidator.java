package com.youbetcha.util;

import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerErrorDto;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDtoValidator {

    private static final int PASSWORD_MINLENGTH = 8;
    private static final int PASSWORD_MAXLENGTH = 50;
    private static final String PASSWORD_REGEXP = "^([\\p{L}\\p{N}]+[.@#$%^&+=]*)*[\\p{L}\\p{N}]*$";

    private static final int FIRST_LAST_NAME_MINLENGTH = 1;
    private static final int FIRST_LAST_NAME_MAXLENGTH = 50;
    private static final String FIRST_LAST_NAME_REGEXP = "^([\\p{L}\\s]+[-.,'\\s]*)*[\\p{L}\\s]*$";

    private static final int MOBILE_MINLENGTH = 8;
    private static final int MOBILE_MAXLENGTH = 20;

    private static final int CITY_MINLENGTH = 3;
    private static final int CITY_MAXLENGTH = 30;
    private static final String CITY_REGEXP = "^([\\p{L}\\s]+[-.,'\\s]*)*[\\p{L}\\s]*$";

    private static final int ADDRESS_MINLENGTH = 10;
    private static final int ADDRESS_MAXLENGTH = 150;
    private static final String ADDRESS_REGEXP = "^([\\p{L}\\p{N}\\s]+[-.,'\\s]*)*[\\p{L}\\p{N}\\s]*$";

    private static final int POSTAL_CODE_MINLENGTH = 1;
    private static final int POSTAL_CODE_MAXLENGTH = 25;
    private static final String POSTAL_CODE_REGEXP = "^([\\p{L}\\p{N}\\s]+[.\\s]*)*[\\p{L}\\p{N}\\s]*$";

    public static List<PlayerErrorDto> validate(PlayerDto player) {
        List<PlayerErrorDto> playerErrors = new ArrayList<>();

        LocalDate now = LocalDate.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate dateOfBirth = LocalDate.parse(player.getBirthDate(), formatter);
            Period period = Period.between(dateOfBirth, now);
            if (period.getYears() < 18 || period.getYears() == 18 && dateOfBirth.getMonthValue() < now.getMonthValue() ||
                    period.getYears() == 18 && dateOfBirth.getMonthValue() == now.getMonthValue()
                            && dateOfBirth.getDayOfMonth() > now.getDayOfMonth())
                playerErrors.add(getError("birthDate", "globalMessages.age.small"));
        } catch (DateTimeParseException e) {
            playerErrors.add(getError("birthDate", "globalMessages.invalid"));
        }

        String validationError = InputValidator.validateStringInputWithRegexp
                (player.getPassword(), PASSWORD_MINLENGTH, PASSWORD_MAXLENGTH, PASSWORD_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("password", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getFirstName(), FIRST_LAST_NAME_MINLENGTH, FIRST_LAST_NAME_MAXLENGTH, FIRST_LAST_NAME_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("firstName", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getLastName(), FIRST_LAST_NAME_MINLENGTH, FIRST_LAST_NAME_MAXLENGTH, FIRST_LAST_NAME_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("lastName", validationError));

        validationError = InputValidator
                .validateStringInputDigitsOnly(player.getMobile(), MOBILE_MINLENGTH, MOBILE_MAXLENGTH);

        if (!validationError.equals("")) playerErrors.add(getError("mobile", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getCity(), CITY_MINLENGTH, CITY_MAXLENGTH, CITY_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("city", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getAddress1(), ADDRESS_MINLENGTH, ADDRESS_MAXLENGTH, ADDRESS_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("address1", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getAddress2(), 0, ADDRESS_MAXLENGTH, ADDRESS_REGEXP, false);

        if (!validationError.equals("")) playerErrors.add(getError("address2", validationError));

        validationError = InputValidator.validateStringInputWithRegexp
                (player.getPostalCode(), POSTAL_CODE_MINLENGTH, POSTAL_CODE_MAXLENGTH, POSTAL_CODE_REGEXP);

        if (!validationError.equals("")) playerErrors.add(getError("postalCode", validationError));

        return playerErrors;
    }

    private static PlayerErrorDto getError(String fieldName, String keyword) {
        PlayerErrorDto error = new PlayerErrorDto();
        error.setField(fieldName);
        error.setKeyword(keyword);
        return error;
    }
}
