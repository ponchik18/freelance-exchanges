package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.freelancer.FreelancerRequest;
import com.bsuir.dto.freelancer.FreelancerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "freelancer-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/freelancers"
)
public interface FreelancerFeignClient {
    @PostMapping
    FreelancerResponse createFreelancer(@RequestBody FreelancerRequest freelancerRequest);
}