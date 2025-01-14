package com.hanghae.infrastructure.adapter;

import com.hanghae.application.port.out.MovieRepositoryPort;
import com.hanghae.infrastructure.repository.MovieRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieRepositoryAdapter implements MovieRepositoryPort {
    private final MovieRepositoryJpa repository;

    @Override
    public List<Object[]> findShowingMovieSchedules() {
        return repository.findShowingMovieSchedules();
    }
}
