package com.bsuir.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bsuir.constant.FreelanceServiceConstant.Validation.Message.NOT_EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerRequest {
    @NotBlank(message = NOT_EMPTY)
    private String firstName;
    @NotBlank(message = NOT_EMPTY)
    private String lastName;
    @NotBlank(message = NOT_EMPTY)
    private String profilePicture;
    @NotBlank(message = NOT_EMPTY)
    private String email;
    @NotBlank(message = NOT_EMPTY)
    private String userId;
}