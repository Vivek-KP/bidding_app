package com.bidderApp.bidz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {
    public String emailOrPhoneNumber;
    public String password;
}
