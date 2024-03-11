package com.bsuir.repository;

import com.bsuir.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Mono<Customer> findByEmail(String email);
}