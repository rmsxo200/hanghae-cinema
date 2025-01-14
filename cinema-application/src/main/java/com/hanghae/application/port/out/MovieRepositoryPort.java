package com.hanghae.application.port.out;

import java.util.List;

public interface MovieRepositoryPort {
    List<Object[]> findShowingMovieSchedules();
}
