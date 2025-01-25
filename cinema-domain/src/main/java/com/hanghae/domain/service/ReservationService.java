package com.hanghae.domain.service;

import com.hanghae.domain.model.Member;
import com.hanghae.domain.model.ScreenSeatLayout;
import com.hanghae.domain.model.ScreeningSchedule;
import com.hanghae.domain.model.TicketReservation;
import com.hanghae.domain.model.enums.ScreenSeat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    public List<TicketReservation> generateTicketReservationsBySeat(ScreeningSchedule screeningSchedule, Member member, ScreenSeat screenSeat, ScreenSeatLayout screenSeatLayout, int seatCount) {
        //시작 좌석과 연결돤 좌석 모두 가져오기
        List<ScreenSeat> selectedSeats = ScreenSeat.getSelectedConnectedSeats(screenSeat, seatCount);

        String seatRow = screenSeatLayout.getSeatRow(); // 좌석 행
        Long maxSeatNumber = screenSeatLayout.getMaxSeatNumber(); // 좌석 최대 갯수

        // 선택된 좌석이 max_seat_number 범위를 초과하는지 확인
        for (ScreenSeat seat : selectedSeats) {
            if (seat.getSeatNumber() > maxSeatNumber) {
                throw new IllegalArgumentException(
                        String.format("선택한 좌석 %s은(는) 예매 가능한 좌석 범위를 초과합니다. (최대 %s 좌석까지 가능)",
                                seat.name(), seatRow + String.format("%02d", maxSeatNumber)));
            }
        }

        // 예매 리스트 리턴
        return selectedSeats.stream()
                .map(seat -> new TicketReservation(
                        null,
                        seat,
                        screeningSchedule,
                        member, member.getMemberId(),
                        null,
                        null,
                        null
                ))
                .collect(Collectors.toList());
    }
}
