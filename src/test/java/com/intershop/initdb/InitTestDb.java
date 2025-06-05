package com.intershop.initdb;

import com.intershop.domain.Cart;
import com.intershop.domain.Item;
import com.intershop.domain.Orders;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InitTestDb {

    @Autowired
    protected EntityManager entityManager;

    protected Long cartId;
    protected Long itemInCartId;
    protected Long orderId;

    @BeforeEach
    void beforeEach() {
        Item item_1 = new Item();
        item_1.setTitle("item_1 title");
        item_1.setDescription("item_1 description");
        item_1.setPrice(1d);
        entityManager.persist(item_1);

        Item item_2 = new Item();
        item_2.setTitle("item_2 title");
        item_2.setDescription("item_2 description");
        item_2.setPrice(2d);
        entityManager.persist(item_2);


        Orders order_1 = new Orders();
        entityManager.persist(order_1);
        orderId = order_1.getId();

        Cart cart_1 = new Cart();
        cart_1.setItemId(item_1.getId());
        cart_1.setCount(1);
        cart_1.setOrderId(order_1.getId());
        entityManager.persist(cart_1);


        Cart cart_2 = new Cart();
        cart_2.setItemId(item_2.getId());
        cart_2.setCount(2);
        entityManager.persist(cart_2);
        cartId = cart_2.getId();
        itemInCartId = cart_2.getItemId();

        entityManager.flush();
    }

}
