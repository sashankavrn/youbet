package com.youbetcha.model.response;

import com.youbetcha.model.Accounts;
import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountsResponse {

    private List<Accounts> accounts;
    private ErrorData errorData;
    private String isNegativeBalance;
    private int success;
    private String timestamp;
    private String version;
}
