package com.youbetcha.service;

import com.youbetcha.model.Language;
import com.youbetcha.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    @Autowired
    LanguageRepository repository;

    public List<Language> getLanguageList() {
        return (List<Language>) repository.findAll();
    }

    public List<Language> getActiveLanguageList(Boolean active) {
        return (List<Language>) repository.findAllActiveLanguages(active);
    }
}
