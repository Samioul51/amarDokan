package com.amarDokan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.amarDokan.amarDokan.models.Cart;
import com.amarDokan.amarDokan.models.User;
import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public Cart findByProductIdAndUserId(Long productId, Long userId);

    public Integer countByUserId(Long userId);

    public List<Cart> findByUserId(Long userId);

    @Transactional
    @Modifying
    public void deleteByUser(User user);

}
