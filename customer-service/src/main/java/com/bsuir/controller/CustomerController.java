package com.bsuir.controller;

import com.bsuir.dto.CustomerCreateRequest;
import com.bsuir.dto.CustomerUpdateRequest;
import com.bsuir.entity.Customer;
import com.bsuir.service.CustomerService;
import com.bsuir.util.PageSetting;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public Mono<Page<Customer>> getAllCustomers(PageSetting pageSetting) {
        return customerService.getAllCustomers(pageSetting);
    }

    @GetMapping("{id}")
    public Mono<Customer> getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> createCustomer(@RequestBody @Valid CustomerCreateRequest customerCreateRequest) {
        return customerService.saveCustomer(customerCreateRequest);
    }

    @PutMapping("{id}")
    public Mono<Customer> updateCustomer(@RequestBody @Valid CustomerUpdateRequest customerUpdateRequest, @PathVariable String id) {
        return customerService.updateCustomer(id, customerUpdateRequest);
    }
}