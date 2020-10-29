package com.youbetcha.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youbetcha.model.BonusUser;

@Repository
public interface BonusUserRepository extends CrudRepository<BonusUser, Long> {
	
	@Query("SELECT bu from BonusUser bu where bu.userId in (:userIds)")
	Set<BonusUser> findBonusUsersByUserIds(@Param("userIds") Set<Long> userIds);

	@Query("SELECT bu from BonusUser bu where bu.bonus.id = :bonusId")
	Set<BonusUser> findBonusUserByBonusId(@Param("bonusId") Long bonusId);
	
}
