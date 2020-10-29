package com.youbetcha.repository;

import com.youbetcha.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    List<PushNotification> findByContentAndEmailAddress(String content, String emailAddress);

    List<PushNotification> findByEmailAddressAndSeen(String emailAddress, boolean seen);
}
