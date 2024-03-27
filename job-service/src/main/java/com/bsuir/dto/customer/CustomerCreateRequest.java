package com.bsuir.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String userId;
}