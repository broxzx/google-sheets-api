package com.project.googlesheetsapi.entity;

import com.project.googlesheetsapi.entity.enums.LeadSource;
import com.project.googlesheetsapi.model.Budget;
import com.project.googlesheetsapi.model.Country;
import com.project.googlesheetsapi.model.DynamicFields;
import com.project.googlesheetsapi.model.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "leads")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Lead {

    @Id
    private String _id;

    private String companyId;
    private String columnId;
    private String name;
    private LeadSource source;
    private PhoneNumber phoneNumber;
    private Country country;
    private Budget budget;
    private String agent;
    private LocalDateTime lastUpdate;
    private LocalDateTime create;
    private Integer position;
    private Set<String> tagIds;
    private List<Object> chatData;
    private List<DynamicFields> dynamicFields;

}
