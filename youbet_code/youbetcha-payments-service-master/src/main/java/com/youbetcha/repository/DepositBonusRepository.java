package com.youbetcha.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.DepositBonus;

@Repository
public interface DepositBonusRepository extends CrudRepository<DepositBonus, Long> {
	
	Optional<DepositBonus> findByCode(String bonusCode);
	
	List<DepositBonus> findAllByEnabled(Boolean enabled);

	@Query("SELECT db from DepositBonus db, BonusCountry bc WHERE bc.bonus.id = db.id AND bc.countryCode = :countryCode")
	List<DepositBonus> findByCountryCode(@Param("countryCode") String countryCode);

	@Query("SELECT db from DepositBonus db, BonusUser bu WHERE bu.bonus.id = db.id AND bu.userId = :userId")
	List<DepositBonus> findByUserId(@Param("userId") Long id);
	
}
