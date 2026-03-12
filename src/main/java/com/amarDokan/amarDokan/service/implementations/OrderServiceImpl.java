package com.amarDokan.amarDokan.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amarDokan.amarDokan.models.Cart;
import com.amarDokan.amarDokan.models.OrderAddress;
import com.amarDokan.amarDokan.models.OrderRequest;
import com.amarDokan.amarDokan.models.ProductOrder;
import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.CartRepository;
import com.amarDokan.amarDokan.repository.ProductOrderRepository;
import com.amarDokan.amarDokan.service.OrderService;

//import com.amarDokan.amarDokan.util.CommonUtil;
import com.amarDokan.amarDokan.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    //@Autowired
    //private CommonUtil commonUtil;

    @Override
    public void saveOrder(Long userid, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid);

        OrderAddress address = new OrderAddress();
        address.setFirstName(orderRequest.getFirstName());
        address.setLastName(orderRequest.getLastName());
        address.setEmail(orderRequest.getEmail());
        address.setMobileNo(orderRequest.getMobileNo());
        address.setAddress(orderRequest.getAddress());
        address.setCity(orderRequest.getCity());
        address.setState(orderRequest.getState());
        address.setPincode(orderRequest.getPincode());

        for (Cart cart : carts) {
            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());

            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            order.setOrderAddress(address);

            orderRepository.save(order);
        }

        if (!carts.isEmpty())
            resetCart(carts.get(0).getUser());
    }

    private void resetCart(User user) {
        cartRepository.deleteByUser(user);
    }

    @Override
    public List<ProductOrder> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public ProductOrder updateOrderStatus(Long id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            return orderRepository.save(productOrder);
        }
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAll(pageable);
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

}
