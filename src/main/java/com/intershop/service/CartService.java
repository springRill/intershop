package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.dto.ItemActionEnum;
import com.intershop.repository.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Mono<Void> changeCartItem(Long itemId, ItemActionEnum action) {
        return cartRepository.findByItemIdAndOrderIdIsNull(itemId)
                .next() // берём первый элемент (если есть)
                .defaultIfEmpty(new Cart(null, itemId, 0, null)) // если не найден — создаём новый Cart
                .flatMap(cart -> {
                    Integer count = cart.getCount() == null ? 0 : cart.getCount();

                    return switch (action) {
                        case PLUS -> {
                            count++;
                            cart.setCount(count);
                            yield cartRepository.save(cart).then();
                        }
                        case MINUS -> {
                            count--;
                            if (count <= 0 && cart.getId() != null) {
                                yield cartRepository.deleteById(cart.getId());
                            } else if (count > 0) {
                                cart.setCount(count);
                                yield cartRepository.save(cart).then();
                            } else {
                                yield Mono.empty();
                            }
                        }
                        case DELETE -> {
                            if (cart.getId() != null) {
                                yield cartRepository.deleteById(cart.getId());
                            } else {
                                yield Mono.empty();
                            }
                        }
                    };
                });
    }

}
