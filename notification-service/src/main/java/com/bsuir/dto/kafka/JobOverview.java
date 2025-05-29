package com.bsuir.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOverview {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private BigDecimal budget;
}
