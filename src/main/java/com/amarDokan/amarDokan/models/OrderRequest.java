package com.amarDokan.amarDokan.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNo;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String paymentType;
}
