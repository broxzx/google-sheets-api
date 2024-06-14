package com.project.googlesheetsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    private Double priceFrom;
    private Double priceTo;

}