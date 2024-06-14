package com.project.googlesheetsapi.service;

import com.project.googlesheetsapi.entity.Lead;
import com.project.googlesheetsapi.entity.enums.LeadSource;
import com.project.googlesheetsapi.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;

    public Lead saveLead(Lead lead) {
        lead.setLastUpdate(LocalDateTime.now());
        lead.setCreate(LocalDateTime.now());
        lead.setSource(LeadSource.SHEETS);
        lead.setPosition(1);
        return leadRepository.save(lead);
    }

}
