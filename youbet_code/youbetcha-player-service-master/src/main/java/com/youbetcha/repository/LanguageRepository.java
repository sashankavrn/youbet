package com.youbetcha.repository;

import com.youbetcha.model.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    @Query("SELECT l FROM Language l WHERE l.active = :active")
    Collection<Language> findAllActiveLanguages(@Param("active") Boolean active);
}
