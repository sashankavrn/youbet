package com.youbetcha.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @JsonIgnore
    private String userName;
    @JsonIgnore
    private String ip;

    @Override
    public String toString() {
        return "LoginDto{" +
                "email='" + email + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
