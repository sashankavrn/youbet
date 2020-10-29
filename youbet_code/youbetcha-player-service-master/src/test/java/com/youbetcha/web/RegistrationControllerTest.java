package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.service.RegistrationService;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService service;
    @Mock
    private PasswordEncoder passwordEncoder;
    private Gson gson = new Gson();

    private PlayerDto generateUserDto() {
        return TestDataHelper.createUserDto();
    }

    private PlayerRegistrationResponseDto generateRegRespDto() {
        return TestDataHelper.createRegistrationResponseDto();
    }

    @Ignore
    @Test
    public void shouldPostSingleUser() throws Exception {
        PlayerDto playerDto = generateUserDto();
        String json = gson.toJson(playerDto);
        PlayerRegistrationResponseDto responseDto = generateRegRespDto();
        when(passwordEncoder.encode(playerDto.getPassword())).thenReturn(anyString());
        when(service.add(playerDto)).thenReturn(Optional.of(responseDto));
        String responseJson = gson.toJson(responseDto);

        this.mockMvc.perform(post("/api/v1/players/")
                .header("x-client-ip", "127.0.0.1")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().json(responseJson));
    }

    @Test
    public void shouldUpdateSingleUser() throws Exception {
        PlayerDto playerDto = generateUserDto();
        String json = gson.toJson(playerDto);
        PlayerRegistrationResponseDto responseDto = generateRegRespDto();
        when(service.update(playerDto, 1L)).thenReturn(Optional.of(responseDto));
        String responseJson = gson.toJson(responseDto);

        this.mockMvc.perform(put("/api/v1/players/" + 1L)
                .header("x-client-ip", "127.0.0.1")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}