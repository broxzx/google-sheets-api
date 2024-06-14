package com.project.googlesheetsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicFields {

    private String fieldId;
    private List<String> values;

}
