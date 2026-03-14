package com.amarDokan.amarDokan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.amarDokan.amarDokan.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public List<User> findByRole(String role);

    public User findByResetToken(String token);

    public Boolean existsByEmail(String email);
}
