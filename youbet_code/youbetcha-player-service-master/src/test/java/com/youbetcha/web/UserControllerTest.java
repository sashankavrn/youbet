package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.service.UserService;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    private Gson gson = new Gson();

    @Test
    public void shouldActivateUser() throws Exception {
        PlayerActivateResponse playerActivateResponse = TestDataHelper.createPlayerActivateResponse();
        when(service.activateUser("hashkey-1")).thenReturn(playerActivateResponse);

        String json = gson.toJson(playerActivateResponse);
        this.mockMvc.perform(get("/api/v1/users/activatePlayer/hashkey-1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}