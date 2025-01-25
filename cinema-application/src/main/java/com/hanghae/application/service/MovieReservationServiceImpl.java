package com.hanghae.application.service;

import com.hanghae.application.dto.ApiResponse;
import com.hanghae.application.dto.MovieReservationRequestDto;
import com.hanghae.application.port.in.MovieReservationService;
import com.hanghae.application.port.out.MemberRepositoryPort;
import com.hanghae.application.port.out.ScreenSeatLayoutRepositoryPort;
import com.hanghae.application.port.out.ScreeningScheduleRepositoryPort;
import com.hanghae.application.port.out.TicketReservationRepositoryPort;
import com.hanghae.domain.model.Member;
import com.hanghae.domain.model.ScreenSeatLayout;
import com.hanghae.domain.model.ScreeningSchedule;
import com.hanghae.domain.model.TicketReservation;
import com.hanghae.domain.model.enums.ScreenSeat;
import com.hanghae.domain.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieReservationServiceImpl implements MovieReservationService {
    private final TicketReservationRepositoryPort ticketReservationRepositoryPort;
    private final ScreeningScheduleRepositoryPort screeningScheduleRepositoryPort;
    private final ScreenSeatLayoutRepositoryPort screenSeatLayoutRepositoryPort;
    private final MemberRepositoryPort memberRepositoryPort;
    private final ReservationService reservationService;

    @Override
    @Transactional
    public ApiResponse<Void> saveMovieReservation(MovieReservationRequestDto requestDto) {
        int seatCount = requestDto.seatCount();
        ScreenSeat screenSeat = requestDto.screenSeat();
        Long scheduleId = requestDto.scheduleId();
        Long memberId = requestDto.memberId();

        //영화 스케줄 가져오기
        ScreeningSchedule screeningSchedule = screeningScheduleRepositoryPort.findById(scheduleId);
        Member member = memberRepositoryPort.findById(memberId);

        // 예매내역 조회
        int reservedTicketCount = ticketReservationRepositoryPort.countByScreeningScheduleIdAndMemberId(scheduleId, memberId);

        //1인당 5개로 예매 제한하기
        if (reservedTicketCount + seatCount > 5) {
            throw new IllegalArgumentException("상영시간별 예매는 최대 5개까지 할 수 있습니다.");
        }

        //해당 상영관 좌석 최대값 가져오기
        ScreenSeatLayout screenSeatLayout = screenSeatLayoutRepositoryPort.findBySeatRowAndScreenId(
                screenSeat.getSeatRow(), screeningSchedule.getScreen().getScreenId());

        // 예매할 좌석 목록
        List<ScreenSeat> selectedSeats = ScreenSeat.getSelectedConnectedSeats(requestDto.screenSeat(), seatCount);

        // 중복 예매 내역 확인하기
        int isDuplicateReservationCount = ticketReservationRepositoryPort.countByScheduleIdAndScreenSeats(scheduleId, selectedSeats);
        if(isDuplicateReservationCount > 0) {
            throw new IllegalArgumentException("선택 죄석은 이미 예매 되었습니다.");
        }

        // 선택 좌석기준으로 예약정보 생성
        List<TicketReservation> ticketReservations =
                reservationService.generateTicketReservationsBySeat(
                        screeningSchedule, member, screenSeat, screenSeatLayout, seatCount);

        //좌석 저장
        //List<TicketReservation> savedTicketReservations = ticketReservationRepositoryPort.saveMovieReservations(ticketReservations);
        return ApiResponse.of("Success", 201);
    }
}
