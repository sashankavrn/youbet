package com.youbetcha.converter;

import com.youbetcha.model.LoginEntity;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginResponseToLoginConverterTest {

    LoginResponseToLoginConverter converter;

    @Before
    public void setUp() {
        converter = new LoginResponseToLoginConverter();
    }

    @Test
    public void shouldConvertFromUserToUserDto() {
        LoginPlayerResponse playerDto = TestDataHelper.createLoginPlayerResponse();
        LoginEntity convert = converter.convert(playerDto);

        //TODO fill in
        assertNotNull(convert);
    }
}
