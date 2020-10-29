package com.youbetcha.repository;

import com.youbetcha.model.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    @Query("SELECT c FROM Currency c WHERE c.active = :active ORDER BY c.description ASC")
    Collection<Currency> findAllActiveCurrencies(@Param("active") Boolean active);
}