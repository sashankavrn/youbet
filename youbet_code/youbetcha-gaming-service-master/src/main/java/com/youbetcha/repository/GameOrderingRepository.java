package com.youbetcha.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.entity.GameOrdering;

@Repository
public interface GameOrderingRepository extends JpaRepository<GameOrdering, Long> {
	
    @Query("SELECT DISTINCT g, go.orderNumber, go.tag FROM Game g, GameOrdering go where go.game.id = g.id AND g.enabled = true " +
            "AND g.restrictedTerritories NOT LIKE (%:countryCode%) " +
    		"AND (:countryCode = go.countryCode OR go.countryCode = 'XX') " +
            "AND g.category = :category " +
    		"AND (go.tag.countryCode = :countryCode OR go.tag.countryCode = 'XX') " + 
            "AND go.tag.enabled = true " +
            "AND g.terminal IN (:terminals) GROUP BY go.id, go.tag.id ORDER BY go.countryCode, go.orderNumber" )
    List<Object[]> findActiveGamesByCountryAndCategoryAndTerminal(
            @Param("countryCode") String countryCode,
            @Param("category") String category,
            @Param("terminals") List<String> terminals,
            Pageable pageable);

    @Query("SELECT DISTINCT g, go.orderNumber FROM Game g, GameOrdering go where go.game.id = g.id AND g.enabled = true " +
            "AND g.restrictedTerritories NOT LIKE (%:countryCode%) " +
    		"AND (:countryCode = go.countryCode OR go.countryCode = 'XX') " +
            "AND go.tag.name = :tag " +
            "AND g.terminal IN (:terminals) GROUP BY go.id, go.tag.id ORDER BY go.countryCode, go.orderNumber" )
    List<Object[]> findActiveGamesByCountryAndTagAndTerminal(
            @Param("countryCode") String countryCode,
            @Param("tag") String tag,
            @Param("terminals") List<String> terminals);

    @Query("SELECT DISTINCT go FROM Game g, GameOrdering go where go.game.id = g.id AND g.enabled = true " +
            "AND g.restrictedTerritories NOT LIKE (%:countryCode%) " +
    		"AND (:countryCode = go.countryCode OR go.countryCode = 'XX') " +
            "AND go.tag.name = :tag " +
            "AND g.terminal IN (:terminals) GROUP BY go.id, go.tag.id ORDER BY go.countryCode, go.orderNumber" )
    List<GameOrdering> findActiveGameOrderingsByCountryAndTagAndTerminal(
            @Param("countryCode") String countryCode,
            @Param("tag") String tag,
            @Param("terminals") List<String> terminals);

//    @Query("SELECT DISTINCT go FROM Game g, GameOrdering go where go.game.id = g.id AND g.enabled = true " +
//            "AND g.restrictedTerritories NOT LIKE (%:countryCode%) " +
//    		"AND (:countryCode = go.countryCode OR go.countryCode = 'XX') " +
//            "AND go.tag.name = :tag " +
//            "AND g.terminal IN (:terminals) GROUP BY go.id, go.tag.id ORDER BY go.countryCode, go.orderNumber" )
    Optional<GameOrdering> findGameOrderingByGameIdAndTagIdAndCountryCode(Long gameId, Long tagId, String countryCode);
}
