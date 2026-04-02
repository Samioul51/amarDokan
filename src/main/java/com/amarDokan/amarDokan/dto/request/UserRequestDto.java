package com.amarDokan.amarDokan.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String mobileNumber;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;

    @NotBlank
    private String password;
}
