package com.youbetcha.service;

import com.youbetcha.model.Currency;
import com.youbetcha.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    CurrencyRepository repository;

    public List<Currency> getCurrencies() {
        return (List<Currency>) repository.findAll();
    }

    public List<Currency> getActiveCurrencyList(Boolean active) {
        return (List<Currency>) repository.findAllActiveCurrencies(active);
    }
}
