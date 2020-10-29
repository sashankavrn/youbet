package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.service.RegistrationService;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService service;

    private Gson gson = new Gson();

    private PlayerDto generateUserDto() {
        return TestDataHelper.createUserDto();
    }

    private PlayerRegistrationResponseDto generateRegRespDto() {
        return TestDataHelper.createRegistrationResponseDto();
    }

    @Test
    public void shouldGetUsers() throws Exception {
        List<PlayerDto> playerDtoList = Collections.singletonList(generateUserDto());
        String json = gson.toJson(playerDtoList);
        when(service.getPlayers()).thenReturn(playerDtoList);

        this.mockMvc.perform(get("/api/v1/admin/players")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void shouldGetSingleUser() throws Exception {
        PlayerDto playerDto = generateUserDto();
        playerDto.setId(1L);
        Long id = playerDto.getId();

        when(service.getById(id)).thenReturn(playerDto);

        String json = gson.toJson(generateUserDto());
        this.mockMvc.perform(get("/api/v1/admin/players/" + id)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }

}