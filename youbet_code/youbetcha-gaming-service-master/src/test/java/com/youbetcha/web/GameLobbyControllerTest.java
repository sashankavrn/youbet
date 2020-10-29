package com.youbetcha.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.TagDto;
import com.youbetcha.service.GameLobbyService;
import com.youbetcha.utils.TestDataHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GameLobbyControllerTest {
    @MockBean
    GameLobbyService gameLobbyService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @Ignore
    public void shouldGetGameByCategoryLobby() throws Exception {
        Map<TagDto, List<GameDto>> allGames = new HashMap<TagDto, List<GameDto>>();
        List<GameDto> gameList = new ArrayList<GameDto>();
        gameList.add(TestDataHelper.createGameDto());
        TagDto tag = new TagDto();
        tag.setName("tag");
        allGames.put(tag, gameList);
        when(gameLobbyService.getGamesByCategory(any(), any(), any(), any())).thenReturn(allGames);

        String json = "{\"metaData\":{\"countryCode\":null,\"tags\":null,\"overridden\":false,\"totalElements\":1,\"totalPages\":1},\"promotedGames\":[{\"id\":null,\"slug\":\"bobby-7s\",\"gameId\":\"Bobby7sFlash\",\"expiryTime\":null,\"gameName\":\"bobby7s\",\"description\":null,\"categories\":[\"VIDEOPOKER\"],\"tags\":null,\"playMode\":false,\"launchGameInHtml5\":false,\"properties\":null,\"open\":false}],\"topGames\":null,\"games\":[{\"id\":null,\"slug\":\"bobby-7s\",\"gameId\":\"Bobby7sFlash\",\"expiryTime\":null,\"gameName\":\"bobby7s\",\"description\":null,\"categories\":[\"VIDEOPOKER\"],\"tags\":null,\"playMode\":false,\"launchGameInHtml5\":false,\"properties\":null,\"open\":false}]}";

        this.mockMvc.perform(get("/api/v1/games/lobby/category/LIVECASINO"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}
