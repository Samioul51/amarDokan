package com.amarDokan.amarDokan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;
}
