package com.youbetcha.web;

import com.youbetcha.model.Language;
import com.youbetcha.service.LanguageService;
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
@RequestMapping("/api/v1/languages")
@Api(tags = "Reference Data")
public class LanguageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);

    @Autowired
    LanguageService service;

    @GetMapping()
    @ApiOperation(value = "Get all languages")
    public ResponseEntity<List<Language>> getLanguageList(@RequestParam(required = false) Boolean active) {
        LOGGER.info("Attempting to fetch language list");
        return active != null ?
                new ResponseEntity<>(service.getActiveLanguageList(active), HttpStatus.OK) :
                new ResponseEntity<>(service.getLanguageList(), HttpStatus.OK);
    }


}
