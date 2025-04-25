package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.BookingRequestDto;
import com.bali.baliairfms.dto.responsedto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto dto);

    BookingResponseDto getBookingById(Long id);

    List<BookingResponseDto> getAllBookings();

    List<BookingResponseDto> getBookingsByPassenger(Long passengerId);

    List<BookingResponseDto> getBookingsByFlight(Long flightId);

    void cancelBooking(Long id);
}
