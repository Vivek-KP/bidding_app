package com.bidderApp.bidz.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private String email;
    private Long phoneNumber;
    private String image;
    private String address;
    private String shippingAddress;
    private int creditBalance;
}
