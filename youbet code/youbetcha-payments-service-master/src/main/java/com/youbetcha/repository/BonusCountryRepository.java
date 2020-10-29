package com.youbetcha.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.BonusCountry;

@Repository
public interface BonusCountryRepository extends CrudRepository<BonusCountry, Long> {
	
	@Query("SELECT bc from BonusCountry bc where bc.countryCode in (:countryCodes)")
	Set<BonusCountry> findBonusCountriesByCountryCodes(@Param("countryCodes") Set<String> countryCodes);
	
	@Query("SELECT bc from BonusCountry bc where bc.bonus.id = :bonusId")
	Set<BonusCountry> findBonusCountryByBonusId(@Param("bonusId") Long bonusId);
	
}
