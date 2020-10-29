package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.service.GameLobbyService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EMGameFeedControllerTest {
    @MockBean
    GameLobbyService gameLobbyService;
    @Autowired
    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Test
    @Ignore
    public void shouldGetGameLobby() throws Exception {
        // when(gameLobbyService.getMixData(anyString())).thenReturn(Optional.of(Collections.emptyList()));

        String json = gson.toJson(new Object());
        this.mockMvc.perform(get("/api/v1/games/lobby")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}
