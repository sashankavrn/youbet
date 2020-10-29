package com.youbetcha.web;

import com.youbetcha.model.Country;
import com.youbetcha.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Api(tags = "Reference Data")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    CountryService service;

    @GetMapping()
    @ApiOperation(value = "Get all countries")
    public ResponseEntity<List<Country>> getCountryList(@RequestParam(required = false) Boolean active) {
        LOGGER.info("Attempting to fetch country list");
        return active != null ?
                new ResponseEntity<>(service.getOrderedActiveCountryList(), HttpStatus.OK) :
                new ResponseEntity<>(service.getCountryList(), HttpStatus.OK);
    }
}
