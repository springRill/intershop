package com.intershop.repository;

import com.intershop.domain.Item;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {

    Mono<Long> countByTitleContaining(String pattern);

    Flux<Item> findByTitleContaining(String pattern, Pageable pageable);

}
