package com.youbetcha.service;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.Player;
import com.youbetcha.model.VerificationToken;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.repository.PlayerRepository;
import com.youbetcha.repository.TokenRepository;

@Service
public class VerificationService {

    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public void createVerificationTokenForUser(final Player player, final String token) {
        final VerificationToken myToken = new VerificationToken(token, player);
        tokenRepository.save(myToken);
    }

    public String validateVerificationToken(String token) {
        Optional<VerificationToken> byToken = tokenRepository.findByToken(token);
        VerificationToken verificationToken = null;
        if (byToken.isPresent()) {
            verificationToken = byToken.get();
        }
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final Player player = verificationToken.getPlayer();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            player.setEnabled(false);
            playerRepository.save(player);
            return TOKEN_EXPIRED;
        }

        player.setEnabled(true);
        playerRepository.save(player);
        tokenRepository.delete(verificationToken);
        return TOKEN_VALID;
    }

    public Player getPlayer(String verificationToken) {
        return tokenRepository.findByToken(verificationToken)
                .map(VerificationToken::getPlayer)
                .orElseThrow(() -> new PlayerNotFoundException("Couldn't find a Player with token: " + verificationToken));
    }

    // Confirm the user is actually enabled in our DB
    public boolean isUserEnabled(LoginDto loginDto) {
        return playerRepository.findEnabledByEmail(loginDto.getEmail()).isPresent();
    }

//    public boolean isUserEnabled(LoginDto loginDto) {
//        return playerRepository.findByEmail(loginDto.getEmail())
//                .flatMap(player -> Optional.ofNullable(tokenRepository.findByPlayer(player))
//                        .filter(x -> !CollectionUtils.isEmpty(x))
//                        .flatMap((Collection<VerificationToken> t) -> t.stream()
//                                .map(VerificationToken::getExpiryDate)
//                                .max(Date::compareTo)
//                                .map(token -> new Date().compareTo(token) <= 0)))
//                .orElse(true);
//    }

}
