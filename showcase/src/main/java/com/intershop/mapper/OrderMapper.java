package com.intershop.mapper;

import com.intershop.domain.Orders;
import com.intershop.dto.OrdersDto;

public class OrderMapper {

    public static OrdersDto toOrdersDto(Orders orders){
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(orders.getId());
        return ordersDto;
    }
}
