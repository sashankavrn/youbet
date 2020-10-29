package com.youbetcha.service;

import com.youbetcha.exceptions.JsonNullOrEmptyException;
import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.model.Player;
import com.youbetcha.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements Authentication {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public String authenticate(String sessionId, String email) {
        //get users details from DB
        Player player = playerRepository.findByEmail(email).orElseThrow(JsonNullOrEmptyException::new);
        return jwtTokenUtil.generateToken(player, sessionId);
    }
}