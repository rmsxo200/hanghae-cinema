package com.hanghae.application.service;

import com.hanghae.application.dto.MovieScheduleResponseDto;
import com.hanghae.application.dto.ShowingMovieScheduleResponseDto;
import com.hanghae.application.port.in.MovieScheduleService;
import com.hanghae.application.port.out.MovieRepositoryPort;
import com.hanghae.application.port.out.ScreeningScheduleRepositoryPort;
import com.hanghae.domain.model.Movie;
import com.hanghae.domain.model.ScreeningSchedule;
import com.hanghae.domain.model.UploadFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final ScreeningScheduleRepositoryPort screeningScheduleRepositoryPort;
    private final MovieRepositoryPort movieRepositoryPort;

    @Override
    @Transactional
    public List<MovieScheduleResponseDto> getMovieSchedules() {
        List<ScreeningSchedule> schedules = screeningScheduleRepositoryPort.findAll();

        return schedules.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ShowingMovieScheduleResponseDto> getShowingMovieSchedules() {
        List<Object[]> results = movieRepositoryPort.findShowingMovieSchedules();

        Map<Long, ShowingMovieScheduleResponseDto> movieMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long movieId = (Long) row[0];

            // 영화가 이미 맵에 존재하는지 확인, 없다면 생성
            ShowingMovieScheduleResponseDto movie = movieMap.computeIfAbsent(movieId, id -> ShowingMovieScheduleResponseDto.builder()
                    .movieId((Long) row[0])
                    .title((String) row[1])
                    .rating((String) row[2])
                    .releaseDate((Date) row[3])
                    .thumbnail((String) row[4])
                    .runningTimeMinutes((Long) row[5])
                    .genre((String) row[6])
                    .build()
            );

            // 스케줄 추가
            movie.addSchedule(ShowingMovieScheduleResponseDto.Schedule.builder()
                            .screenName((String) row[7])
                            .showStartAt((String) row[8])
                            .showEndAt((String) row[9])
                            .build());
        }

        return new ArrayList<>(movieMap.values());
    }

    private MovieScheduleResponseDto convertToDto(ScreeningSchedule schedule) {
        Movie movie = schedule.getMovie();
        String thumbnailPath = getThumbnailPath(movie.getUploadFile());

        return new MovieScheduleResponseDto(
                movie.getTitle(),
                movie.getRating().getCodeName(),
                movie.getReleaseDate(),
                thumbnailPath,
                movie.getRunningTimeMinutes(),
                movie.getGenre().getCodeName(),
                schedule.getScreen().getScreenName(),
                schedule.getShowStartAt()
        );
    }

    private String getThumbnailPath(UploadFile uploadFile) {
        if (uploadFile == null) {
            return "";
        }
        String filePath = Optional.ofNullable(uploadFile.getFilePath()).orElse("");
        String fileName = Optional.ofNullable(uploadFile.getFileName()).orElse("");
        return filePath + fileName;
    }
}
