package com.youbetcha.repository;

import com.youbetcha.model.entity.EMTable;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("classpath:LoadEMTables.sql")
public class EMEMTableRepositoryTest {

    @Autowired
    private EMTableRepository emTableRepository;

    @Test @Ignore
    public void persistTableToDb() {
        EMTable EMTable = TestDataHelper.createTable();
        emTableRepository.save(EMTable);

        Assert.assertNotNull(emTableRepository.findById(EMTable.getId()));
    }

    @Test @Ignore
    public void findAllTables() {
        List<EMTable> gamesList = (List<EMTable>) emTableRepository.findAll();
        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.size() == 3);
    }

    @Test @Ignore
    public void findActiveTables() {
        List<EMTable> gamesList = emTableRepository.findActiveTables();

        Assert.assertNotNull(gamesList);
    }

    @Test @Ignore
    public void findActiveTablesByCountry() {
        List<EMTable> gamesList = emTableRepository.findActiveTablesByCountry("GB");

        Assert.assertNotNull(gamesList);
    }
}
