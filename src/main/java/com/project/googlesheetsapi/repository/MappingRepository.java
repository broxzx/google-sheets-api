package com.project.googlesheetsapi.repository;

import com.project.googlesheetsapi.entity.Mappings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MappingRepository extends MongoRepository<Mappings, String> {

    Optional<Mappings> findMappingsByCompanyId(String companyId);

}
