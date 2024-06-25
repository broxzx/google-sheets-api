package com.project.googlesheetsapi.entity;

import com.google.api.services.sheets.v4.model.Sheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "mappings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mappings {

    private String id;

    private String companyId;

    private Map<String, String> fieldMappings;

}
