package com.youbetcha.repository;

import com.youbetcha.model.Player;
import com.youbetcha.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Collection<VerificationToken> findByPlayer(Player player);
}
