package com.example.workflex.factory;

import com.example.workflex.data.Workation;
import com.example.workflex.data.dto.WorkationDto;
import com.example.workflex.enums.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkationDtoFactoryImplTest {

    private WorkationDtoFactoryImpl factory;

    @BeforeEach
    void setUp() {
        factory = new WorkationDtoFactoryImpl();
    }

    @Test
    void create_ShouldMapAllFields() {
        // arrange
        Workation w = Workation.builder()
                .id(42L)
                .employee("ABC")
                .origin("Germany")
                .destination("Spain")
                .startDate(LocalDate.parse("2023-04-01"))
                .endDate(LocalDate.parse("2023-04-10"))
                .riskLevel(RiskLevel.LOW_RISK)
                .build();

        // act
        WorkationDto dto = factory.create(w);

        // assert
        assertNotNull(dto);
        assertEquals(42L, dto.getId());
        assertEquals("ABC", dto.getEmployeeName());
        assertEquals("Spain", dto.getDestination());
        assertEquals("Germany", dto.getCountryCode());
        assertEquals("01/04/2023", dto.getStartDate());
        assertEquals("10/04/2023", dto.getEndDate());
        assertEquals("LOW_RISK", dto.getRiskLevel());
    }

    @Test
    void create_NullInput_ShouldReturnNull() {
        // arrange

        // act + assert
        assertNull(factory.create(null));
    }
}