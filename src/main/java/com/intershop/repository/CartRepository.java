package com.intershop.repository;

import com.intershop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByOrderIdIsNull();

    List<Cart> findByItemIdAndOrderIdIsNull(Long itemId);

    List<Cart> findByOrderId(Long orderId);

}
