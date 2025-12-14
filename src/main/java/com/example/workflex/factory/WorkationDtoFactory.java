package com.example.workflex.factory;

import com.example.workflex.data.Workation;
import com.example.workflex.data.dto.WorkationDto;

public interface WorkationDtoFactory {
    WorkationDto create(Workation workation);
}
