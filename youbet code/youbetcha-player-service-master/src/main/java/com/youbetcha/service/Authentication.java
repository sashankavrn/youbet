package com.youbetcha.service;

public interface Authentication {
    String authenticate(String sessionId, String email);
}
