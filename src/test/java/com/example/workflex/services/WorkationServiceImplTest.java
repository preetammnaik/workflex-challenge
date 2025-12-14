package com.example.workflex.services;

import com.example.workflex.data.Workation;
import com.example.workflex.data.dto.WorkationDto;
import com.example.workflex.enums.RiskLevel;
import com.example.workflex.exceptions.WorkationDataLoadException;
import com.example.workflex.factory.WorkationDtoFactory;
import com.example.workflex.repository.WorkationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkationServiceImplTest {

    @Mock
    private WorkationRepository mockRepository;

    @Mock
    private WorkationDtoFactory mockWorkationDtoFactory;

    private WorkationServiceImpl workationService;

    @BeforeEach
    void setUp() {
        workationService = new WorkationServiceImpl(mockRepository, mockWorkationDtoFactory);
    }

    @Test
    void getAllWorkations_ShouldReturnListOfWorkationDtos() {
        // arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);

        Workation workation1 = Workation.builder()
                .id(1L)
                .workationId("W001")
                .employee("John Doe")
                .origin("New York")
                .destination("Paris")
                .startDate(startDate)
                .endDate(endDate)
                .workingDays(14)
                .riskLevel(RiskLevel.LOW_RISK)
                .build();

        Workation workation2 = Workation.builder()
                .id(2L)
                .workationId("W002")
                .employee("Jane Smith")
                .origin("London")
                .destination("Tokyo")
                .startDate(startDate.plusDays(10))
                .endDate(endDate.plusDays(10))
                .workingDays(12)
                .riskLevel(RiskLevel.MEDIUM_RISK)
                .build();

        List<Workation> workations = Arrays.asList(workation1, workation2);

        WorkationDto dto1 = new WorkationDto(1L, "John Doe", "New York", "Paris", "01/01/2024", "15/01/2024", "LOW_RISK");
        WorkationDto dto2 = new WorkationDto(2L, "Jane Smith", "London", "Tokyo", "11/01/2024", "25/01/2024", "MEDIUM_RISK");

        when(mockRepository.findAll()).thenReturn(workations);
        when(mockWorkationDtoFactory.create(workation1)).thenReturn(dto1);
        when(mockWorkationDtoFactory.create(workation2)).thenReturn(dto2);

        // act
        List<WorkationDto> result = workationService.getAllWorkations();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(mockRepository, times(1)).findAll();
        verify(mockWorkationDtoFactory, times(1)).create(workation1);
        verify(mockWorkationDtoFactory, times(1)).create(workation2);
    }

    @Test
    void getAllWorkations_WhenRepositoryReturnsEmptyList_ShouldReturnEmptyList() {
        // arrange
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        List<WorkationDto> result = workationService.getAllWorkations();

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockRepository, times(1)).findAll();
        verify(mockWorkationDtoFactory, never()).create(any());
    }

    @Test
    void getAllWorkations_WhenRepositoryThrowsException_ShouldThrowWorkationDataLoadException() {
        // arrange
        String errorMessage = "Database connection failed";
        when(mockRepository.findAll()).thenThrow(new RuntimeException(errorMessage));

        // act & assert
        WorkationDataLoadException exception = assertThrows(WorkationDataLoadException.class, () -> workationService.getAllWorkations());

        assertEquals("Failed to load workations from repository", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(errorMessage, exception.getCause().getMessage());

        verify(mockRepository, times(1)).findAll();
        verify(mockWorkationDtoFactory, never()).create(any());
    }
}