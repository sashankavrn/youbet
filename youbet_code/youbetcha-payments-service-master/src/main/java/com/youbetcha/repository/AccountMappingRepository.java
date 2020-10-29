package com.youbetcha.repository;

import com.youbetcha.model.mapping.AccountMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMappingRepository extends CrudRepository<AccountMapping, Long> {
    List<AccountMapping> findByKeyValue(String merchantReference);
}
