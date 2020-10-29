package com.youbetcha.web;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.model.ProductType;
import com.youbetcha.model.account.AccountBalanceResponse;
import com.youbetcha.model.account.AccountDetailsDto;
import com.youbetcha.model.account.AccountNotificationsDto;
import com.youbetcha.model.account.AccountTransactionsDto;
import com.youbetcha.model.account.AccountUpdateResponse;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.dto.TransactionDto;
import com.youbetcha.model.response.UserAccountsResponse;
import com.youbetcha.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/v1/account")
@Api(tags = "Account")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @PostMapping("/balance")
    @ApiOperation(value = "Get users account balance")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(@RequestParam(required = false) ProductType productType,
                                                                    @RequestParam String sessionId) {
        LOGGER.info("Attempting to fetch users account balance {}", sessionId);
        if (productType == null) {
            productType = ProductType.UNSPECIFIED;
        }
        return new ResponseEntity<>(accountService.accountBalance(sessionId, productType.ordinal()), HttpStatus.OK);
    }

    @PostMapping("/transactions")
    @ApiOperation(value = "Get users transaction history")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(@RequestBody AccountTransactionsDto accountTransactionsDto,
                                                                       @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {
        LOGGER.info("Attempting to fetch users account transactions {}", principal.getName());
        return new ResponseEntity<>(accountService.accountTransactions(principal.getName(), accountTransactionsDto), HttpStatus.OK);
    }

    @PostMapping("/notifications")
    @ApiOperation(value = "Update the player's notification status")
    public ResponseEntity<AccountUpdateResponse> updateAccountNotifications(@RequestBody AccountNotificationsDto dto,
                                                                       @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {
        LOGGER.info("Attempting to update account notifications for {}", principal.getName());
        return new ResponseEntity<AccountUpdateResponse>(accountService.updateNotifications(principal.getName(), dto), HttpStatus.OK);
    }

    @PostMapping("/details")
    @ApiOperation(value = "Update the player's details")
    public ResponseEntity<AccountUpdateResponse> updateAccountDetails(@RequestBody AccountDetailsDto dto,
                                                                       @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {
        LOGGER.info("Attempting to update account notifications for {}", principal.getName());
        return new ResponseEntity<AccountUpdateResponse>(accountService.updateDetails(principal.getName(), dto), HttpStatus.OK);
    }

    @PostMapping("/balance/max")
    @ApiOperation(value = "Gets maximum amount of money a user can deposit")
    public ResponseEntity<MaxDepositResponse> getMaximumDepositAmount(@RequestBody SessionDto sessionDto) {
        LOGGER.info("Attempting to fetch max amount user with id: {} can deposit.", sessionDto.getUserId());
        return new ResponseEntity<>(accountService.checkMaxDepositAmount(sessionDto), HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiOperation(value = "Gets user account")
    public ResponseEntity<UserAccountsResponse> getUserAccounts(@RequestParam String sessionId) {
        LOGGER.info("Attempting to fetch user accounts with session id: {} can deposit.", sessionId);
        return new ResponseEntity<>(accountService.getUserAccounts(sessionId), HttpStatus.OK);
    }

}
