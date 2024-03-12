package com.bsuir.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bsuir.constant.FreelanceServiceConstant.Validation.Message.NOT_EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRequest {
    @NotBlank(message = NOT_EMPTY)
    private String resumeName;
    @NotBlank(message = NOT_EMPTY)
    private String resumeContent;
    @NotNull(message = NOT_EMPTY)
    private Long freelancerId;
}