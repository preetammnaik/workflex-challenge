package com.example.workflex.indexer;

import com.example.workflex.config.WorkationDataLoader;
import com.example.workflex.data.Workation;
import com.example.workflex.repository.WorkationRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class WorkationIndexerImpl implements WorkationIndexer {

    private final WorkationRepository repository;
    private final WorkationDataLoader loader;

    @Autowired
    public WorkationIndexerImpl(WorkationRepository repository, WorkationDataLoader loader) {
        this.repository = repository;
        this.loader = loader;
    }

    @PostConstruct
    public void index() {
        try {
            long count = repository.count();
            if (count > 0) {
                log.info("Workations already indexed (count = {})", count);
                return;
            }
        } catch (Exception e) {
            log.warn("Could not determine repository count, continuing with indexing. Cause: {}", e.getMessage(), e);
        }

        log.info("================== Starting Workation Indexing ==================");

        List<Workation> workations;
        try {
            workations = loader.load();
            if (workations == null || workations.isEmpty()) {
                log.warn("No workations loaded from data source; aborting indexing");
                return;
            }
            log.info("Loaded {} workations from data source", workations.size());
        } catch (Exception e) {
            log.error("Failed to load workations from data source", e);
            return;
        }

        try {
            repository.saveAll(workations);
        } catch (Exception e) {
            log.error("Failed to save workations to the repository", e);
            return;
        }

        try {
            Workation first = workations.get(0);
            Optional<Workation> test = repository.findById(first.getId());
            if (test.isPresent()) {
                log.info("Test retrieval successful for workation: {}", test.get());
            } else {
                log.error("Test retrieval failed for workation with ID: {}", first.getId());
            }
        } catch (Exception e) {
            log.error("Verification retrieval failed", e);
        }

        log.info("Indexed {} workations into database", workations.size());
        log.info("================== Workation Indexing Completed ==================");
    }
}
