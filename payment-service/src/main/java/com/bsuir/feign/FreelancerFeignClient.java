package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.freelancer.FreelancerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "freelancer-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/freelancers"
)
public interface FreelancerFeignClient {
    @GetMapping("{id}")
    FreelancerResponse getFreelancerById(@PathVariable String id);
}