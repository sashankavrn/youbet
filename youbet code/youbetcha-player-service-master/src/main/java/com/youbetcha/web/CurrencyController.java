package com.youbetcha.web;

import com.youbetcha.model.Currency;
import com.youbetcha.service.CurrencyService;
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
@RequestMapping("/api/v1/currencies")
@Api(tags = "Reference Data")
public class CurrencyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    CurrencyService service;

    @GetMapping()
    @ApiOperation(value = "Get all currencies")
    public ResponseEntity<List<Currency>> getCurrencyList(@RequestParam(required = false) Boolean active) {
        LOGGER.info("Attempting to fetch currency list");
        return active != null ?
                new ResponseEntity<>(service.getActiveCurrencyList(active), HttpStatus.OK) :
                new ResponseEntity<>(service.getCurrencies(), HttpStatus.OK);
    }
}
