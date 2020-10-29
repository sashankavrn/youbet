package com.youbetcha.web;

import com.youbetcha.service.Authentication;
import com.youbetcha.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/login")
@Api(tags = "Admin")
public class LoginAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAdminController.class);

    @Autowired
    LoginService service;
    @Autowired
    Authentication authenticationService;

    @ApiOperation(value = "Get failed login response by user id")
    @GetMapping("/{userName}")
    public ResponseEntity<List<String>> getFailedResponseByUserId(@PathVariable String userName) {
        LOGGER.info("Attempting to fetch failed response for Player with Username: {}", userName);
        Pageable pageable = PageRequest.of(0, 10);
        Page<String> responses = service.getFailedResponse(pageable, userName);
        return new ResponseEntity<>(responses.get().collect(Collectors.toList()), HttpStatus.OK);
    }
}
