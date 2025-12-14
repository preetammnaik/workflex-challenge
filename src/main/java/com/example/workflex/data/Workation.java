package com.example.workflex.data;

import com.example.workflex.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "workations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String workationId;

    private String employee;

    private String origin;

    private String destination;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer workingDays;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
}
