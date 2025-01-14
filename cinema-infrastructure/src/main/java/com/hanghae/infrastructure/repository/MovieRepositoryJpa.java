package com.hanghae.infrastructure.repository;

import com.hanghae.infrastructure.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepositoryJpa extends JpaRepository<MovieEntity, Long> {
    @Query(value = "SELECT " +
                "    mv.movie_id, " +
                "    mv.title, " +
                "    mv.rating, " +
                "    mv.release_date, " +
                "    CONCAT(uf.file_path, '/', uf.file_name) AS thumbnail, " +
                "    mv.running_time_minutes, " +
                "    mv.genre, " +
                "    sn.screen_name, " +
                "    DATE_FORMAT(ss.show_start_at, '%Y-%m-%d %H:%i') AS show_start_at, " +
                "    DATE_FORMAT(DATE_ADD(ss.show_start_at, INTERVAL mv.running_time_minutes MINUTE), '%Y-%m-%d %H:%i') AS show_end_at " +
                "FROM movie mv " +
                "    INNER JOIN screening_schedule ss " +
                "        ON ss.movie_id = mv.movie_id " +
                "    INNER JOIN screen sn " +
                "        ON sn.screen_id = ss.screen_id " +
                "    LEFT JOIN upload_file uf " +
                "        ON uf.file_id = mv.file_id " +
                "WHERE ss.show_start_at > NOW() " +
                "ORDER BY release_date DESC" ,
            nativeQuery = true)
    List<Object[]> findShowingMovieSchedules();
}
