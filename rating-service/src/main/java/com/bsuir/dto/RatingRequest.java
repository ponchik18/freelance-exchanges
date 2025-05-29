package com.bsuir.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bsuir.constant.RatingServiceConstant.Validation.Message.NOT_EMPTY;
import static com.bsuir.constant.RatingServiceConstant.Validation.Message.NOT_VALID_RANGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @NotNull(message = NOT_EMPTY)
    @Min(value = 1, message = NOT_VALID_RANGE)
    @Max(value = 5, message = NOT_VALID_RANGE)
    private Integer rating;
    @NotBlank(message = NOT_EMPTY)
    private String review;
    @NotBlank(message = NOT_EMPTY)
    private String fromUser;
    @NotBlank(message = NOT_EMPTY)
    private String toUser;
    private Long jobId;
}