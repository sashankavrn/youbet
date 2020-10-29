package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.service.Authentication;
import com.youbetcha.service.LoginService;
import com.youbetcha.service.VerificationService;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Authentication authenticationService;
    @Mock
    private VerificationService verificationService;
    @MockBean
    private LoginService service;

    private Gson gson = new Gson();

    @Test
    public void shouldLoginPlayer() throws Exception {
        LoginPlayerResponse loginPlayerResponse = TestDataHelper.createLoginPlayerResponse();
        LoginDto loginDto = TestDataHelper.createLogin();

        when(verificationService.isUserEnabled(loginDto)).thenReturn(true);
        when(authenticationService.authenticate(any(), any())).thenReturn("header");
        when(service.loginUser(loginDto)).thenReturn(Optional.ofNullable(loginPlayerResponse));
        String loginJson = gson.toJson(loginDto);

        this.mockMvc.perform(post("/api/v1/login/")
                .header("x-client-ip", "127.0.0.1")
                .contentType(MediaType.APPLICATION_JSON).content(loginJson)).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldLogoutPlayer() throws Exception {
        LogoutPlayerResponse logoutPlayerResponse = TestDataHelper.createLogoutPlayerResponse();
        String json = gson.toJson(logoutPlayerResponse);

        when(service.logout("1")).thenReturn(logoutPlayerResponse);

        this.mockMvc.perform(get("/api/v1/login/logout/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}