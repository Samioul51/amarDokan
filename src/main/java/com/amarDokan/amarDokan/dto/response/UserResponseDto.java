package com.amarDokan.amarDokan.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String profileImage;
    private String role;
    private Boolean isEnable;
    private Boolean accountNonLocked;
}
