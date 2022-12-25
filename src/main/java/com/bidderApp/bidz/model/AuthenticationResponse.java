package com.bidderApp.bidz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String jwt;
    private final String id;
    private final String name;
    private final String status;
    private final int statusCode;
}
