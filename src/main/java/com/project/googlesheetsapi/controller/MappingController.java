package com.project.googlesheetsapi.controller;

import com.project.googlesheetsapi.entity.Mappings;
import com.project.googlesheetsapi.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mapping")
@RequiredArgsConstructor
public class MappingController {

    private final MappingService mappingService;

    @PostMapping
    public Mappings createMapping(@RequestBody Mappings mapping) {
        return mappingService.createMapping(mapping);
    }

    @GetMapping("/{companyId}")
    public Mappings getMapping(@PathVariable String companyId) {
        return mappingService.getMappingsByCompanyId(companyId)
                .orElseThrow();
    }

    @GetMapping
    public List<Mappings> getAllMappings() {
        return mappingService.getMappings();
    }

}