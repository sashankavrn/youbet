package com.youbetcha.service;

import com.youbetcha.model.Player;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return playerRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                        new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Player getPlayer(LoginDto loginDto) {
        return playerRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " +
                        loginDto.getEmail()));
    }
}