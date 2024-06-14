package com.project.googlesheetsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    private String countryCode;
    private String phoneNumber;

}
