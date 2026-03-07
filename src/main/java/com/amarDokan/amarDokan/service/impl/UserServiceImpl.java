package com.amarDokan.amarDokan.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.UserRepository;
import com.amarDokan.amarDokan.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    // Account lock duration in minutes
    private static final long lockDurationMinutes = 15;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Long id, Boolean status) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void increaseFailedAttempt(User user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(User user) {
        LocalDateTime lockTime = user.getLockTime();
        LocalDateTime unlockTime = lockTime.plusMinutes(lockDurationMinutes);

        if (LocalDateTime.now().isAfter(unlockTime)) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resetAttempt(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        foundUser.ifPresent(user -> {
            user.setFailedAttempt(0);
            userRepository.save(user);
        });
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetToken(resetToken);
            userRepository.save(user);
        }
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(User user, MultipartFile img) {
        User dbUser = userRepository.findById(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(dbUser)) {
            dbUser.setName(user.getName());
            dbUser.setMobileNumber(user.getMobileNumber());
            dbUser.setAddress(user.getAddress());
            dbUser.setCity(user.getCity());
            dbUser.setState(user.getState());
            dbUser.setPincode(user.getPincode());

            if (!img.isEmpty())
                dbUser.setProfileImage(img.getOriginalFilename());

            dbUser = userRepository.save(dbUser);

            try {
                if (!img.isEmpty()) {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator
                            + "profile_img" + File.separator + img.getOriginalFilename());
                    Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dbUser;
    }

    @Override
    public User saveAdmin(User user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
