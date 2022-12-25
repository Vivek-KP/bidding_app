
package com.bidderApp.bidz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private String id;
    private String name;
    private String username;
    private String email;
    private String role = "ROLE_ADMIN";
    private Long phoneNumber;
    private String image;
    private int creditBalance;
}
