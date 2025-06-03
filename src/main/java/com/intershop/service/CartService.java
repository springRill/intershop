package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.dto.ItemActionEnum;
import com.intershop.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void changeCartItem(Long itemId, ItemActionEnum action) {
        List<Cart> cartList = cartRepository.findByItemIdAndOrderIdIsNull(itemId);
        Cart cart;
        if(cartList.isEmpty()){
            cart = new Cart();
            cart.setItemId(itemId);
        }else{
            cart = cartList.getFirst();
        }
        Integer count = cart.getCount();
        switch (action) {
            case PLUS -> {
                System.out.println("PLUS");
                count++;
                cart.setCount(count);
                cartRepository.save(cart);
            }
            case MINUS -> {
                System.out.println("MINUS");
                count--;
                if (count <= 0 && cart.getId() != null) {
                    cartRepository.deleteById(cart.getId());
                } else if (count > 0) {
                    cart.setCount(count);
                    cartRepository.save(cart);
                }
            }
            case DELETE -> {
                System.out.println("DELETE");
                cartRepository.deleteById(cart.getId());
            }
        }
    }

}
