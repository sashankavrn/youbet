package com.youbetcha.repository;

import com.youbetcha.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM Message m where m.user_id = ?1 AND m.seen = ?2", nativeQuery = true)
    List<Message> findByStatusAndByUserId(@Param("userId") long userId, @Param("seen") boolean seen);
}
