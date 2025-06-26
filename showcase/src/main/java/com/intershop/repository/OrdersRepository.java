package com.intershop.repository;

import com.intershop.domain.Orders;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends R2dbcRepository<Orders, Long> {
}
