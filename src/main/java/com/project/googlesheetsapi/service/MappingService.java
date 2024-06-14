package com.project.googlesheetsapi.service;

import com.project.googlesheetsapi.entity.Mappings;
import com.project.googlesheetsapi.repository.MappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MappingService {

    private final MappingRepository mappingRepository;


    public Mappings createMapping(Mappings mapping) {
        return mappingRepository.save(mapping);
    }

    public Optional<Mappings> getMappingsByCompanyId(String companyId) {
        return mappingRepository.findMappingsByCompanyId(companyId);
    }

    public List<Mappings> getMappings() {
        return mappingRepository.findAll();
    }

}
