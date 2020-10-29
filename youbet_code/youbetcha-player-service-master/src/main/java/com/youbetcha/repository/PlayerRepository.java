package com.youbetcha.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Optional<Player> findByEmail(String userName);

    // Workaround MariaDB bug where the default query always returns true
    @Query("SELECT p from Player p where p.enabled = true and p.email = :email")
    Optional<Player> findEnabledByEmail(@Param("email") String email);

}