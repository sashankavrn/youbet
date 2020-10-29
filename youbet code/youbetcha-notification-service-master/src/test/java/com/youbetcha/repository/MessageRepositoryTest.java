package com.youbetcha.repository;

import com.youbetcha.model.Message;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("classpath:LoadMessages.sql")
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void persistGameToDb() {
        Message message = TestDataHelper.createMessage();
        messageRepository.save(message);

        Assert.assertNotNull(messageRepository.findById(message.getId()));
    }

    @Test
    public void findActiveMessagesByEmail() {
        List<Message> messagesList = messageRepository.findByStatusAndByUserId(3, false);

        Assert.assertNotNull(messagesList);
    }
}
