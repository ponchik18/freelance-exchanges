package com.bsuir.service;

import com.bsuir.dto.CustomerCreateRequest;
import com.bsuir.dto.CustomerUpdateRequest;
import com.bsuir.entity.Customer;
import com.bsuir.exception.CustomerNotFoundException;
import com.bsuir.exception.DuplicateEmailException;
import com.bsuir.mapper.CustomerMapper;
import com.bsuir.repository.CustomerRepository;
import com.bsuir.util.PageSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Mono<Page<Customer>> getAllCustomers(PageSetting pageSetting) {
        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());

        return customerRepository.findAll()
                .collectList()
                .zipWith(this.customerRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    public Mono<Customer> saveCustomer(CustomerCreateRequest customerCreateRequest) {
        return customerRepository.findByEmail(customerCreateRequest.getEmail())
                .flatMap(existingCustomer -> Mono.error(new DuplicateEmailException(customerCreateRequest.getEmail())))
                .then(customerRepository.save(customerMapper.toEntity(customerCreateRequest)));
    }

    public Mono<Customer> getCustomerById(String id) {
        return customerRepository.findByUserId(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
    }

    public Mono<Customer> updateCustomer(String id, CustomerUpdateRequest customerUpdateRequest) {
        return getCustomerById(id)
                .flatMap(existingCustomer -> {
                    if (!existingCustomer.getEmail().equals(customerUpdateRequest.getEmail())) {
                        return customerRepository.findByEmail(customerUpdateRequest.getEmail())
                                .flatMap(validateDuplicateEmail(id, existingCustomer))
                                .switchIfEmpty(Mono.just(existingCustomer));
                    }
                    return Mono.just(existingCustomer);
                })
                .flatMap(existingCustomer -> customerRepository.save(updateAfterValidate(existingCustomer, customerUpdateRequest)));
    }

    private Function<Customer, Mono<? extends Customer>> validateDuplicateEmail(String id, Customer existingCustomer) {
        return customerWithEmail -> {
            if (!customerWithEmail.getUserId().equals(id)) {
                return Mono.error(new DuplicateEmailException(customerWithEmail.getEmail()));
            }
            return Mono.just(existingCustomer);
        };
    }

    private Customer updateAfterValidate(Customer customer, CustomerUpdateRequest customerUpdateRequest) {
        customer.setEmail(customerUpdateRequest.getEmail());
        customer.setFirstName(customerUpdateRequest.getFirstName());
        customer.setLastName(customerUpdateRequest.getLastName());
        customer.setProfilePicture(customerUpdateRequest.getProfilePicture());
        return customer;
    }
}