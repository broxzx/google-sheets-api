package com.project.googlesheetsapi.controller;

import com.project.googlesheetsapi.entity.Lead;
import com.project.googlesheetsapi.entity.Mappings;
import com.project.googlesheetsapi.model.Budget;
import com.project.googlesheetsapi.model.Country;
import com.project.googlesheetsapi.model.DynamicFields;
import com.project.googlesheetsapi.model.PhoneNumber;
import com.project.googlesheetsapi.service.LeadService;
import com.project.googlesheetsapi.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;
    private final MappingService mappingService;
    private static final Map<Integer, String> mapKeysToValues;

    static {
        mapKeysToValues = new HashMap<>(Map.of(
                0, "companyId",
                1, "columnId",
                2, "name",
                3, "phoneNumber",
                4, "country",
                5, "price",
                6, "tags",
                7, "dynamicField"
        ));
    }

    @PostMapping("/receive/integration")
    public void receiveFormData(@RequestBody Map<String, Object> payload) {
        String companyId = (String) payload.get("companyId");
        String sheetName = (String) payload.get("sheetName");
        List<Object> values = (List<Object>) payload.get("values");

        System.out.println(companyId);
        System.out.println(sheetName);
        System.out.println(values);

        Optional<Mappings> mapping = mappingService.getMappingsByCompanyId(companyId);

        List<DynamicFields> dynamicFieldsList = Arrays.stream(((String) values.get(10)).split(";"))
                .map(dynamicField -> dynamicField.split(":"))
                .filter(parts -> parts.length == 2)
                .map(parts -> {
                    String fieldId = parts[0].trim();
                    List<String> fieldValues = Arrays.stream(parts[1].split(","))
                            .map(String::trim)
                            .toList();
                    return new DynamicFields(fieldId, fieldValues);
                })
                .toList();

        Lead lead = Lead.builder()
                .companyId(companyId)
                .columnId((String) values.get(1))
                .name((String) values.get(2))
                .phoneNumber(new PhoneNumber(String.valueOf(values.get(3)), String.valueOf(values.get(4))))
                .country(new Country((String) values.get(5), (String) values.get(6)))
                .budget(new Budget((double) ((Integer) values.get(7)), (double) ((Integer) values.get(8))))
                .tagIds(Set.of(Arrays.stream(((String) values.get(9)).split(",")).map(String::trim).toString()))
                .dynamicFields(dynamicFieldsList)
                .build();

        mapping.ifPresentOrElse(mappings -> {
            Map<String, String> fieldMappings = mappings.getFieldMappings();

            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String field = entry.getKey(); // name -> companyId
                Integer index = mapKeysToValues.entrySet().stream()
                        .filter(e -> e.getValue().equals(entry.getValue()))
                        .findAny()
                        .get()
                        .getKey();

                if (index == null) {
                    throw new RuntimeException("key '%s' is not found".formatted(field));
                }

                switch (field) {
                    case "companyId":
                        lead.setCompanyId((String) values.get(index));
                        break;
                    case "columnId":
                        lead.setColumnId((String) values.get(index));
                        break;
                    case "name":
                        lead.setName((String) values.get(index));
                        break;
                    case "phoneNumber":
                        PhoneNumber phoneNumber = new PhoneNumber();
                        phoneNumber.setPhoneNumber(String.valueOf(((Integer) values.get(index))));
                        lead.setPhoneNumber(phoneNumber);
                        break;
                    case "country":
                        Country country = new Country();
                        country.setCountryName((String) values.get(index));
                        lead.setCountry(country);
                        break;
                    case "budget":
                        Budget budget = new Budget();
                        budget.setPriceFrom((double) ((Integer) values.get(index)));
                        lead.setBudget(budget);
                        break;
                    case "tags":
                        lead.setTagIds(Set.of(((String) values.get(index)).split(",")));
                        break;
                    case "dynamicFields":
                        String[] dynamicFieldsData = ((String) values.get(index)).split(";");
                        List<DynamicFields> dynamicFieldsListCustom = new ArrayList<>();
                        for (String dynamicField : dynamicFieldsData) {
                            String[] parts = dynamicField.split(":");
                            if (parts.length == 2) {
                                DynamicFields dynamicFieldObj = new DynamicFields();
                                dynamicFieldObj.setFieldId(parts[0].trim());
                                dynamicFieldObj.setValues(Stream.of(parts[1].split(","))
                                        .map(String::trim)
                                        .toList());
                                dynamicFieldsListCustom.add(dynamicFieldObj);
                            }
                        }
                        lead.setDynamicFields(dynamicFieldsListCustom);
                        break;
                }

                System.out.println(leadService.saveLead(lead));
            }
        }, () -> {
            leadService.saveLead(lead);
        });


    }

    @GetMapping("/receive/integration")
    public String hello() {
        return "hello";
    }

}
