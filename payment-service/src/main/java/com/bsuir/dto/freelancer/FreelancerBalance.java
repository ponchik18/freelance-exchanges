package com.bsuir.dto.freelancer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerBalance {
    private BigDecimal balance;
}