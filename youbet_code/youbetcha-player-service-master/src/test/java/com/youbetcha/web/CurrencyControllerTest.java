package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.service.CurrencyService;
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
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService service;

    private Gson gson = new Gson();

    @Test
    public void shouldGetCurrencies() throws Exception {

        when(service.getCurrencies()).thenReturn(TestDataHelper.createCurrencies());

        String json = gson.toJson(TestDataHelper.createCurrencies());
        this.mockMvc.perform(get("/api/v1/currencies")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}