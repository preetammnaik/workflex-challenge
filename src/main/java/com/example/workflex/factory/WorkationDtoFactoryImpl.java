package com.example.workflex.factory;

import com.example.workflex.data.Workation;
import com.example.workflex.data.dto.WorkationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class WorkationDtoFactoryImpl implements WorkationDtoFactory {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public WorkationDto create(Workation workation) {
        if (workation == null) {
            log.info("Received null workation to convert to DTO");
            return null;
        }

        return new WorkationDto(
                workation.getId(),
                workation.getEmployee(),
                workation.getDestination(),
                workation.getOrigin(),
                workation.getStartDate().format(DATE_FORMATTER),
                workation.getEndDate().format(DATE_FORMATTER),
                workation.getRiskLevel().name()
        );
    }
}
