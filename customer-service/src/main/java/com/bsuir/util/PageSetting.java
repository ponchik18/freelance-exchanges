package com.bsuir.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bsuir.constant.CustomerServiceConstant.DefaultValue.ELEMENT_PER_PAGE;
import static com.bsuir.constant.CustomerServiceConstant.DefaultValue.PAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private int pageNumber = PAGE;
    private int elementsPerPage = ELEMENT_PER_PAGE;
}