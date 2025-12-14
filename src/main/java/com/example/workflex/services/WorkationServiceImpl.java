package com.example.workflex.services;

import com.example.workflex.data.dto.WorkationDto;
import com.example.workflex.exceptions.WorkationDataLoadException;
import com.example.workflex.factory.WorkationDtoFactory;
import com.example.workflex.repository.WorkationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkationServiceImpl implements WorkationService {
    private final WorkationRepository repository;
    private final WorkationDtoFactory workationDtoFactory;

    @Autowired
    public WorkationServiceImpl(WorkationRepository repository, WorkationDtoFactory workationDtoFactory) {
        this.repository = repository;
        this.workationDtoFactory = workationDtoFactory;
    }

    @Override
    public List<WorkationDto> getAllWorkations() {
        try {
            return repository.findAll()
                    .stream()
                    .map(workationDtoFactory::create)
                    .toList();
        } catch (Exception e) {
            throw new WorkationDataLoadException("Failed to load workations from repository", e);
        }
    }
}
