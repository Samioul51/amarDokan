package com.amarDokan.amarDokan.mapper;

import com.amarDokan.amarDokan.dto.request.AdminCreateRequestDto;
import com.amarDokan.amarDokan.dto.request.UserProfileUpdateDto;
import com.amarDokan.amarDokan.dto.request.UserRequestDto;
import com.amarDokan.amarDokan.dto.response.UserResponseDto;
import com.amarDokan.amarDokan.models.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setMobileNumber(dto.getMobileNumber());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setPincode(dto.getPincode());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static User toEntity(AdminCreateRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMobileNumber(dto.getMobileNumber());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setPincode(dto.getPincode());
        return user;
    }

    public static void updateEntity(UserProfileUpdateDto dto, User user) {
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setMobileNumber(dto.getMobileNumber());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setPincode(dto.getPincode());
    }

    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setPincode(user.getPincode());
        dto.setProfileImage(user.getProfileImage());
        dto.setRole(user.getRole());
        dto.setIsEnable(user.getIsEnable());
        dto.setAccountNonLocked(user.getAccountNonLocked());
        return dto;
    }
}
