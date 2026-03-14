package com.amarDokan.amarDokan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.amarDokan.amarDokan.models.OrderAddress;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}
