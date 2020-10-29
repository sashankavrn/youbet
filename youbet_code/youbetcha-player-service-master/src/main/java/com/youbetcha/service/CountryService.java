package com.youbetcha.service;

import com.youbetcha.model.Country;
import com.youbetcha.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    CountryRepository repository;

    public List<Country> getCountryList() {
        return (List<Country>) repository.findAll();
    }

    public List<Country> getActiveCountryList(Boolean active) {
        return (List<Country>) repository.findAllActiveCountries(active);
    }
    
    public List<Country> getOrderedActiveCountryList() {
    	return (List<Country>) repository.findAllOrderedActiveCountries();
    }
}
