package com.amarDokan.amarDokan.service;

import java.util.List;
import com.amarDokan.amarDokan.models.Cart;

public interface CartService {

    public Cart saveCart(Long productId, Long userId);

    public List<Cart> getCartsByUser(Long userId);

    public Integer getCountCart(Long userId);

    public void updateQuantity(String action, Long cartId);

}
