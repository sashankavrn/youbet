package com.youbetcha.repository;

import com.youbetcha.model.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    @Query("SELECT c FROM Country c WHERE c.active = :active ORDER BY c.name ASC")
    Collection<Country> findAllActiveCountries(@Param("active") Boolean active);

    @Query("SELECT c FROM Country c WHERE c.active = true ORDER BY c.orderNumber ASC")
    Collection<Country> findAllOrderedActiveCountries();

}
