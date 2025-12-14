package com.example.workflex.controller;

import com.example.workflex.data.dto.WorkationDto;
import com.example.workflex.exceptions.WorkationDataLoadException;
import com.example.workflex.services.WorkationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/workflex/workation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkationController {

    private final WorkationService workationService;

    public WorkationController(WorkationService workationService) {
        this.workationService = workationService;
    }

    @GetMapping(path = "/workation", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<WorkationDto>> getAllWorkations() {
        try {
            log.info("Fetching all workations");
            return ResponseEntity.ok(workationService.getAllWorkations());

        } catch (Exception e) {
            log.error("Error while retreiving all workations!", e);
            throw new WorkationDataLoadException("Failed to determine location for the point", e);
        }
    }
}
