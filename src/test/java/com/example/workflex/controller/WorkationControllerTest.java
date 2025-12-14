package com.example.workflex.controller;

import com.example.workflex.data.dto.WorkationDto;
import com.example.workflex.exceptions.WorkationDataLoadException;
import com.example.workflex.services.WorkationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkationControllerTest {

    @Mock
    private WorkationService mockWorkationService;

    private WorkationController workationController;

    @BeforeEach
    void setUp() {
        workationController = new WorkationController(mockWorkationService);
    }

    @Test
    void getAllWorkations_ShouldReturnListOfWorkations() {
        // arrange
        WorkationDto dto1 = new WorkationDto(1L, "ABC", "New York", "Paris", "01/01/2024", "15/01/2024", "LOW_RISK");
        WorkationDto dto2 = new WorkationDto(2L, "XYZ", "London", "Tokyo", "11/01/2024", "25/01/2024", "MEDIUM_RISK");

        List<WorkationDto> workations = Arrays.asList(dto1, dto2);

        when(mockWorkationService.getAllWorkations()).thenReturn(workations);

        // act
        ResponseEntity<List<WorkationDto>> response = workationController.getAllWorkations();

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(dto1, response.getBody().get(0));
        assertEquals(dto2, response.getBody().get(1));

        verify(mockWorkationService, times(1)).getAllWorkations();
    }

    @Test
    void getAllWorkations_WhenServiceReturnsEmptyList_ShouldReturnEmptyList() {
        // arrange
        when(mockWorkationService.getAllWorkations()).thenReturn(Collections.emptyList());

        // act
        ResponseEntity<List<WorkationDto>> response = workationController.getAllWorkations();

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(mockWorkationService, times(1)).getAllWorkations();
    }

    @Test
    void getAllWorkations_WhenServiceThrowsWorkationDataLoadException_ShouldThrowSameException() {
        // arrange

        when(mockWorkationService.getAllWorkations()).thenThrow(new WorkationDataLoadException("ERROR!"));

        // act & assert
        WorkationDataLoadException thrownException = assertThrows(WorkationDataLoadException.class, () -> workationController.getAllWorkations());

        verify(mockWorkationService, times(1)).getAllWorkations();
    }
}