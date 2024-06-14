package com.project.googlesheetsapi.repository;

import com.project.googlesheetsapi.entity.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeadRepository extends MongoRepository<Lead, String> {



}
