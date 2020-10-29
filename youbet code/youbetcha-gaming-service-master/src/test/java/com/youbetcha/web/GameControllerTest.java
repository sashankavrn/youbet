package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.service.LaunchGameService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {
    @MockBean
    LaunchGameService launchGameService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Gson gson = new Gson();

    @Test @Ignore
    public void shouldGetGame() throws Exception {
        when(jwtTokenUtil.getSessionIdFromToken(any())).thenReturn("testsessionID");
        when(launchGameService.launchCasinoGame(any(), anyString())).thenReturn("Http://mygameurl");

        //String json = gson.toJson("Http://mygameurl");
        this.mockMvc.perform(get("/api/v1/games")).andDo(print()).andExpect(status().is4xxClientError());
        //.andExpect(content().json(json));
    }
}
