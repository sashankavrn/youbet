package com.youbetcha.web;

import com.youbetcha.service.GeoIPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/geoip")
@Api(tags = "Reference Data")
public class GeoIPController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeoIPController.class);

    @Autowired
    GeoIPService service;

    @ApiOperation(value = "Get ISO2 country code by id")
    @GetMapping("/{id}")
    public ResponseEntity<String> getCountryISO2ByIP(@PathVariable String id) {
        String iso2CountryCode = service.getCountryISO2ByIP(id);
        return new ResponseEntity<>(iso2CountryCode, HttpStatus.OK);
    }
}
