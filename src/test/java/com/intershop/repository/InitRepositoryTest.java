package com.intershop.repository;

import com.intershop.domain.Cart;
import com.intershop.domain.Item;
import com.intershop.domain.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class InitRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void beforeEach() {
        Item item_1 = new Item();
        item_1.setTitle("item_1 title");
        item_1.setDescription("item_1 description");
        item_1.setPrice(1d);
        item_1 = testEntityManager.persist(item_1);

        Item item_2 = new Item();
        item_2.setTitle("item_2 title");
        item_2.setDescription("item_2 description");
        item_2.setPrice(2d);
        item_2 = testEntityManager.persist(item_2);


        Orders order_1 = new Orders();
        order_1 = testEntityManager.persist(order_1);

        Cart cart_1 = new Cart();
        cart_1.setItemId(item_1.getId());
        cart_1.setCount(1);
        cart_1.setOrderId(order_1.getId());
        testEntityManager.persist(cart_1);


        Cart cart_2 = new Cart();
        cart_2.setItemId(item_2.getId());
        cart_2.setCount(2);
        testEntityManager.persist(cart_2);

    }

}
