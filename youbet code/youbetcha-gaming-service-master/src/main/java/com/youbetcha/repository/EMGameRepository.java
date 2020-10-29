package com.youbetcha.repository;

import com.youbetcha.model.entity.EMGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EMGameRepository extends JpaRepository<EMGame, Long> {

    @Query("SELECT g FROM EMGame g where g.enabled = true AND (:countryCode) NOT IN g.restrictedTerritories ORDER BY g.gameName")
    List<EMGame> findActiveGamesByCountry(@Param("countryCode") String countryCode);

    @Query("SELECT g FROM EMGame g where g.enabled = true  ORDER BY g.gameName")
    List<EMGame> findActiveGames();

    Optional<EMGame> findByDataId(Long dataId);
}
