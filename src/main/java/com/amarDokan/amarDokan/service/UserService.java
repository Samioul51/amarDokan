package com.amarDokan.amarDokan.service;

import com.amarDokan.amarDokan.dto.request.AdminCreateRequestDto;
import com.amarDokan.amarDokan.dto.request.UserProfileUpdateDto;
import com.amarDokan.amarDokan.dto.request.UserRequestDto;
import com.amarDokan.amarDokan.dto.response.UserResponseDto;
import com.amarDokan.amarDokan.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public User saveUserFromDto(UserRequestDto dto, String profileImage);

    public User saveAdminFromDto(AdminCreateRequestDto dto, String profileImage);

    public User getUserByEmail(String email);

    public List<User> getUsers(String role);

    List<UserResponseDto> getUserDtos(String role);

    public Boolean updateAccountStatus(Long id, Boolean status);

    public void increaseFailedAttempt(User user);

    public void userAccountLock(User user);

    public boolean unlockAccountTimeExpired(User user);

    public void resetAttempt(Long userId);

    public void updateUserResetToken(String email, String resetToken);

    public User getUserByToken(String token);

    public User updateUser(User user);

    public User updateUserProfile(User user, MultipartFile img);

    public User updateUserProfileFromDto(UserProfileUpdateDto dto, MultipartFile img);

    public User saveAdmin(User user);

    public Boolean existsEmail(String email);

}
