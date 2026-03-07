package com.amarDokan.amarDokan.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.amarDokan.amarDokan.models.OrderRequest;
import com.amarDokan.amarDokan.models.ProductOrder;

public interface OrderService {

    public void saveOrder(Long userid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Long userId);

    public ProductOrder updateOrderStatus(Long id, String status);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
