package com.youbetcha.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.entity.Game;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g where g.enabled = true AND " +
            "(:countryCode is null OR :countryCode NOT IN g.restrictedTerritories) " +
            "AND (:category is null or g.category = :category) " +
            "AND g.terminal IN (:terminals) ")
    Page<Game> findActiveGamesByCountryAndCategoryAndTerminal(
            @Param("countryCode") String countryCode,
            @Param("category") String category,
            @Param("terminals") List<String> terminals,
            Pageable pageable);

    @Query("SELECT g FROM Game g where g.enabled = true ORDER BY g.gameName")
    Page<Game> findActiveGames(Pageable pageable);

    Optional<Game> findByDataId(Long dataId);

    Optional<Game> findBySlug(String slug);

    @Query("SELECT g.category from Game g where g.category = :category")
    List<String> findDistinctTags(@Param("category") String category);

}
