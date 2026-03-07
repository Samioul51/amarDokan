package com.amarDokan.amarDokan.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String mobileNumber;

    @Column(unique = true, nullable = false)
    private String email;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String password;

    private String profileImage;

    private String role;

    private Boolean isEnable = true;

    private Boolean accountNonLocked = true;

    private Integer failedAttempt = 0;

    private LocalDateTime lockTime;

    private String resetToken;
}
