package com.youbetcha.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/admin")
@Api(tags = "Admin")
public class ProfileCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileCheckController.class);

    @Autowired
    private org.springframework.core.env.Environment environment;

    @ApiOperation(value = "Check Profile")
    @GetMapping("/checkProfile")
    public String[] checkProfile() {
        String[] activeProfiles = environment.getActiveProfiles();      // it will return String Array of all active profile.
        Arrays.stream(activeProfiles).forEach(profile -> LOGGER.info("profile is {} ", profile));
        return activeProfiles;
    }
}
