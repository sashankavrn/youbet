package com.youbetcha.repository;

import com.youbetcha.model.mapping.PlayerMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMappingRepository extends CrudRepository<PlayerMapping, Long> {
    List<PlayerMapping> findByKeyValue(String merchantReference);
}
