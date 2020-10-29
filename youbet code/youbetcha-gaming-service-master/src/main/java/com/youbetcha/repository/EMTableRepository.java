package com.youbetcha.repository;

import com.youbetcha.model.entity.EMTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EMTableRepository extends CrudRepository<EMTable, Long> {
    @Query("SELECT g FROM EMTable g where g.enabled = true AND (:countryCode) NOT IN g.restrictedTerritories ORDER BY g.tableName")
    List<EMTable> findActiveTablesByCountry(@Param("countryCode") String countryCode);

    @Query("SELECT g FROM EMTable g where g.enabled = true ORDER BY g.tableName")
    List<EMTable> findActiveTables();

    Optional<EMTable> findByTableId(Long tableId);
}
