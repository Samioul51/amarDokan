package com.amarDokan.amarDokan.service.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.amarDokan.amarDokan.models.Cart;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.CartRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;
import com.amarDokan.amarDokan.repository.UserRepository;
import com.amarDokan.amarDokan.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Long productId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(product))
            return null;

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart;

        if (ObjectUtils.isEmpty(cartStatus)) {
            // if product is not in the cart it will be added as new item
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(product.getDiscountPrice());
        } else {
            // if product is already in the cart it will increase the quantity
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }

        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartsByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        Double totalOrderPrice = 0.0;
        List<Cart> updatedCarts = new ArrayList<>();

        for (Cart c : carts) {
            if (c.getProduct() != null) {
                Double discountPrice = c.getProduct().getDiscountPrice();
                if (discountPrice == null) {
                    discountPrice = c.getProduct().getPrice();
                }
                Double totalPrice = discountPrice * c.getQuantity();
                c.setTotalPrice(totalPrice);
                totalOrderPrice += totalPrice;
                c.setTotalOrderPrice(totalOrderPrice);
            }
            updatedCarts.add(c);
        }

        return updatedCarts;
    }

    @Override
    public Integer getCountCart(Long userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(String action, Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (ObjectUtils.isEmpty(cart))
            return;

        if (action.equalsIgnoreCase("de")) {
            // Decreasing quantity
            int updatedQuantity = cart.getQuantity() - 1;

            if (updatedQuantity <= 0) {
                // if quantity becomes 0 it will be removed from the cart
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updatedQuantity);
                cartRepository.save(cart);
            }
        } else {
            // Increasing quantity
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        }
    }

}
