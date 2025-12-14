package com.example.workflex.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkationDto {

    private Long id;
    private String employeeName;
    private String destination;
    private String countryCode;
    private String startDate;
    private String endDate;
    private String riskLevel;
}
