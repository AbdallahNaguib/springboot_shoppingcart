package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order,Long> {
}
