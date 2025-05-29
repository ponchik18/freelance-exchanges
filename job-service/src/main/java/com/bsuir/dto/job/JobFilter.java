package com.bsuir.dto.job;

import com.bsuir.enums.SortValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobFilter {
    private List<Long> skillIds;
    private String search;
    private BigDecimal startBudget;
    private BigDecimal endBudget;
    private SortValue sort;
}