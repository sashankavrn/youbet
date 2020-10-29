package com.youbetcha.mocks;

import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.model.Player;
import com.youbetcha.service.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("ci")
public class MockAuthenticationService implements Authentication {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String authenticate(String sessionId, String email)  {
        Player player = new Player();
        player.setEmail("test@email.com");
        player.setFirstName("test");
        player.setLastName("user");
        return jwtTokenUtil.generateToken(player, sessionId);
    }
}
