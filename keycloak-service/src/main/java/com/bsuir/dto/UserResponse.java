package com.bsuir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private String role;
    private Long createdTimestamp;
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String userId;
}