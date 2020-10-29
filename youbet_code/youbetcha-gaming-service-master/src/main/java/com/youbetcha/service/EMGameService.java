package com.youbetcha.service;

import com.youbetcha.exceptions.GameException;
import com.youbetcha.model.entity.EMGame;
import com.youbetcha.model.entity.EMTable;
import com.youbetcha.repository.EMGameRepository;
import com.youbetcha.repository.EMTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EMGameService {

    @Autowired
    EMGameRepository emGameRepository;

    @Autowired
    EMTableRepository emTableRepository;

    public EMGame findGameById(Long id) {
        return emGameRepository.findById(id)
                .orElseThrow(() -> new GameException("Cannot find game with id: " + id));
    }

    public EMTable findTableById(Long id) {
        return emTableRepository.findById(id)
                .orElseThrow(() -> new GameException("Cannot find table with id: " + id));
    }
}
