package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.BookingRequestDto;
import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.dto.responsedto.BookingResponseDto;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.BookingMapper;
import com.bali.baliairfms.model.Booking;
import com.bali.baliairfms.model.Flight;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.model.enums.BookingStatus;
import com.bali.baliairfms.model.enums.NotificationType;
import com.bali.baliairfms.repository.BookingRepository;
import com.bali.baliairfms.repository.FlightRepository;
import com.bali.baliairfms.repository.PassengerRepository;
import com.bali.baliairfms.service.BookingService;
import com.bali.baliairfms.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;


    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        if (bookingRepository.existsByPassenger_IdAndFlight_Id(dto.passengerId(), dto.flightId())) {
            throw new DuplicateResourceException("Booking already exists for this passenger and flight.");
        }

        Passenger passenger = passengerRepository.findById(dto.passengerId())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        Flight flight = flightRepository.findById(dto.flightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        Booking booking = Booking.builder()
                .passenger(passenger)
                .flight(flight)
                .seatNumber(dto.seatNumber())
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.BOOKED)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        notificationService.send(new NotificationRequest(
                passenger.getId(),
                "Booking Confirmed",
                "Your flight " + flight.getFlightNumber() + " has been booked successfully.",
                NotificationType.EMAIL
        ));

        return bookingMapper.toDto(savedBooking);


    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingMapper.toDtoList(bookingRepository.findAll());
    }

    @Override
    public List<BookingResponseDto> getBookingsByPassenger(Long passengerId) {
        return bookingMapper.toDtoList(bookingRepository.findByPassenger_Id(passengerId));
    }

    @Override
    public List<BookingResponseDto> getBookingsByFlight(Long flightId) {
        return bookingMapper.toDtoList(bookingRepository.findByFlight_Id(flightId));
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        notificationService.send(new NotificationRequest(
                booking.getPassenger().getId(),
                "Booking Cancelled",
                "Your booking for flight " + booking.getFlight().getFlightNumber() + " has been cancelled.",
                NotificationType.EMAIL
        ));
    }


}
