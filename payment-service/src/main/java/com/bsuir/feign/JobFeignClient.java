package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.freelancer.FreelancerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(
        name = "job-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/jobs"
)
public interface JobFeignClient {
    @PutMapping("/pay/{id}")
    void payJob(@PathVariable long id);
}