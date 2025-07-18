package com.intershop.service;

import com.intershop.domain.Cart;
import com.intershop.dto.ItemActionEnum;
import com.intershop.repository.CartRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "itemPages", allEntries = true),
            @CacheEvict(value = "carts", allEntries = true),
            @CacheEvict(value = "items", allEntries = true)
    })
    public Mono<Void> changeCartItem(Long itemId, ItemActionEnum action, Long userId) {
        return cartRepository.findByItemIdAndUserIdAndOrderIdIsNull(itemId, userId)
                .next()
                .defaultIfEmpty(new Cart(null, itemId, 0, null, userId))
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
