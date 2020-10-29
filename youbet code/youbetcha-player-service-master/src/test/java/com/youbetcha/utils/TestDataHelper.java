package com.youbetcha.utils;

import com.youbetcha.model.Country;
import com.youbetcha.model.Currency;
import com.youbetcha.model.Language;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.*;
import com.youbetcha.model.response.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TestDataHelper {

    public static PlayerDto createUserDto() {
        return PlayerDto.builder()
                .firstName("test")
                .lastName("user")
                .email("testuser@test.com")
                .password("testtest")
                .address1("blahblahblah")
                .address2("blahblahblah")
                .birthDate("1972-12-07")
                .mobilePrefix("+44")
                .mobile("795678988")
                .city("Landan")
                .countryCode("UK")
                .postalCode("SG85BN")
                .signupIp("127.0.0.1")
                .currency("GBP")
                .bonusOptIn(true)
                .acceptTCs(true)
                .allowSmsNewsAndOffers(true)
                .allowNewsAndOffers(true)
                .build();
    }

    public static PlayerReq createPlayerReq() {
        return PlayerReq.builder()
                .firstName("test")
                .lastName("user")
                .email("testuser@test.com")
                .password("testtest")
                .address1("blahblahblah")
                .address2("blahblahblah")
                .birthDate("1972-12-07")
                .mobilePrefix("+44")
                .mobile("795678988")
                .city("Landan")
                .countryCode("UK")
                .postalCode("SG85BN")
                .signupIp("127.0.0.1")
                .currency("GBP")
                .allowSmsNewsAndOffers("1")
                .allowNewsAndOffers("1")
                .build();
    }

    public static Player createUser() {
        return Player.builder()
                .firstName("test")
                .lastName("user")
                .email("testuser@test.com")
                .password("testtest")
                .address1("blahblahblah")
                .address2("blahblahblah")
                .birthDate("1972-12-07")
                .mobilePrefix("+44")
                .mobile("795678988")
                .city("Landan")
                .countryCode("UK")
                .postalCode("SG85BN")
                .signupIp("127.0.0.1")
                .currency("GBP")
                .acceptTermsAndConditions(true)
                .bonusOptIn(true)
                .allowNewsAndOffers(Boolean.TRUE)
                .allowSmsNewsAndOffers(Boolean.TRUE)
                .enabled(Boolean.TRUE)
                .build();
    }

    public static PlayerRegistrationResponseDto createRegistrationResponseDto() {
        return PlayerRegistrationResponseDto.builder()
                .errorData(ErrorData.builder()
                        .errorCode((short) 1)
                        .errorDetails(Collections.singletonList(""))
                        .logId(1)
                        .build())
                .success((short) 0)
                .timestamp("timestamp")
                .userId(1L)
                .version("1.0")
                .build();
    }

    public static PlayerHashDto createUserHashDto() {
        return PlayerHashDto.builder()
                .Userid("someUserId")
                .URL("http://someurl")
                .build();
    }

    public static RegistrationFieldAvailableResponse createRegistrationFieldAvailableResponse() {
        return RegistrationFieldAvailableResponse.builder()
                .success(1)
                .isAvailable(1)
                .timestamp("2020-06-05")
                .build();
    }

    public static LoginPlayerResponse createLoginPlayerResponse() {
        com.youbetcha.model.ErrorData errorData = com.youbetcha.model.ErrorData.builder()
                .errorCode((short) 1)
                .errorDetails(Collections.emptyList())
                .logId(1)
                .build();
        return LoginPlayerResponse.builder()
                .errorData(errorData)
                .success((short) 1)
                .lastName("user")
                .firstName("test")
                .timestamp("2020-06-05")
                .build();
    }

    public static LogoutPlayerResponse createLogoutPlayerResponse() {
        return LogoutPlayerResponse.builder()
                .success((short) 1)
                .timestamp("2020-06-05")
                .build();
    }

    public static LoginDto createLogin() {
        return LoginDto.builder()
                .email("some@user.com")
                .password("password")
                .ip("someip")
                .build();
    }

    public static PlayerRegistrationResponse createRegistrationResponse() {
        return PlayerRegistrationResponse.builder()
                .success((short) 1)
                .build();
    }

    public static List<Currency> createCurrencies() {
        Currency currencyEuro = new Currency();
        currencyEuro.setCode("EUR");
        currencyEuro.setDescription("Euro");
        List<Currency> currencyList = new LinkedList<>();
        currencyList.add(currencyEuro);
        return currencyList;
    }

    public static List<Country> createCountries() {
        return Collections.singletonList(Country.builder()
                .iso2("PL")
                .iso3("POL")
                .phoneCode("48")
                .name("Poland")
                .build());
    }

    public static List<Language> createLanguages() {
        return Collections.singletonList(Language.builder()
                .locale("ru")
                .description("Russian")
                .build());
    }

    public static PlayerActivateResponse createPlayerActivateResponse() {
        return PlayerActivateResponse.builder()
                .success((short) 1)
                .build();
    }
}
