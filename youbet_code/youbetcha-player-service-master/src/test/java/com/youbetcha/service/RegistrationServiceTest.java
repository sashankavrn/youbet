package com.youbetcha.service;

import com.youbetcha.client.RegistrationClient;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.model.dto.PlayerReq;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import com.youbetcha.repository.PlayerRepository;
import com.youbetcha.service.handler.ResponseHandler;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private RegistrationClient registrationClient;
    @Mock
    private ConversionService converter;
    @Mock
    private ResponseHandler responseHandler;
    @InjectMocks
    private RegistrationService service;

    @Before
    public void setUp() {
        when(converter.convert(any(PlayerDto.class), eq(Player.class))).thenReturn(new Player());
        when(converter.convert(any(PlayerDto.class), eq(PlayerReq.class))).thenReturn(new PlayerReq());
        when(converter.convert(any(PlayerRegistrationResponse.class), eq(PlayerRegistrationResponseDto.class))).thenReturn(new PlayerRegistrationResponseDto());
        when(playerRepository.save(any(Player.class))).thenReturn(new Player());
        doNothing().when(responseHandler).validateResponseSuccess(any(PlayerRegistrationResponse.class), any(Player.class));
        when(registrationClient.registerUser(any(PlayerReq.class))).thenReturn(Optional.of(TestDataHelper.createRegistrationResponse()));
    }

    @Test
    public void shouldReturnResponseWhenSaveUser() {
        PlayerDto playerDto = TestDataHelper.createUserDto();
        
        Optional<PlayerRegistrationResponseDto> add = service.add(playerDto);

        Assert.assertEquals(add.get().getUserId(), playerDto.getId());
    }
}
