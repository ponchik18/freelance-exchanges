package com.bsuir.dto.freelancer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreelancerRequest {
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String userId;
}