package com.hanghae.application.port.out.repository;

import com.hanghae.domain.model.ScreeningSchedule;

import java.util.List;

public interface ScreeningScheduleRepositoryPort {
    List<ScreeningSchedule> findAll();
    ScreeningSchedule findById(Long id);
}
