package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.UserResponse;
import com.bsuir.dto.freelancer.FreelancerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "freelancer-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/freelancers"
)
public interface FreelancerFeignClient {
    @PostMapping
    UserResponse createFreelancer(@RequestBody FreelancerRequest freelancerRequest);
    @GetMapping("/{userId}")
    UserResponse getFreelancerById(@PathVariable String userId);
}