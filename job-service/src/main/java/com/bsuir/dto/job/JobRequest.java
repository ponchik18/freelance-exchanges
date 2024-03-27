package com.bsuir.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.bsuir.constant.JobServiceConstant.Validation.Message.NOT_EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    @NotBlank(message = NOT_EMPTY)
    private String title;
    @NotBlank(message = NOT_EMPTY)
    private String description;
    @NotNull(message = NOT_EMPTY)
    private BigDecimal budget;
}