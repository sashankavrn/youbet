package com.youbetcha.repository;

import com.youbetcha.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends CrudRepository<Player, Long> {

    @Modifying
    @Query( value = "INSERT INTO register_fail (user_name, response) VALUES (:userName, :response)",
            nativeQuery = true)
    void insertFailedResponse(@Param("userName") String userName, @Param("response") String response);


    @Query(
            value = "SELECT response FROM register_fail WHERE user_name=:userName",
            nativeQuery = true)
    Page<String> findAllFailedResponsePagination(Pageable pageable, @Param("userName") String userName);
}
