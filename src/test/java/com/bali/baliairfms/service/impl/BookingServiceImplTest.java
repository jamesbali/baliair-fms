package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.BookingRequestDto;
import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.dto.responsedto.*;
import com.bali.baliairfms.exception.DuplicateResourceException;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.mapper.BookingMapper;
import com.bali.baliairfms.model.Booking;
import com.bali.baliairfms.model.Flight;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.model.enums.BookingStatus;
import com.bali.baliairfms.model.enums.CrewType;
import com.bali.baliairfms.repository.BookingRepository;
import com.bali.baliairfms.repository.FlightRepository;
import com.bali.baliairfms.repository.PassengerRepository;
import com.bali.baliairfms.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Passenger passenger;
    private Flight flight;
    private Booking booking;
    private BookingRequestDto requestDto;
    private BookingResponseDto responseDto;
    private PassengerResponseDto passengerDto;
    private FlightResponseDto flightDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("john.doe@baliair.com");

        flight = new Flight();
        flight.setId(2L);
        flight.setFlightNumber("BA123");

        booking = Booking.builder()
                .id(99L)
                .passenger(passenger)
                .flight(flight)
                .seatNumber("12A")
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.BOOKED)
                .build();

        requestDto = new BookingRequestDto(1L, 2L, "12A");

        passengerDto = new PassengerResponseDto(1L, "john.doe@baliair.com", "John", "Doe", "123456", true);

        AircraftResponseDto aircraftDto = new AircraftResponseDto(
                3L,                      // id
                "TAIL123",               // tailNumber
                "Boeing 747",            // model
                300,                     // seatingCapacity
                13000,                   // rangeKm
                "JFK",                   // currentLocation
                List.of()                // maintenanceRecords
        );



        Set<CrewMemberResponseDto> crewSet = Set.of(
                new CrewMemberResponseDto(
                        5L,
                        "Jane",
                        "Smith",
                        "CREW001",
                        "jane.smith@baliair.com",
                        Set.of("Certified"),
                        Set.of("Trained"),
                        true,
                        800,
                        CrewType.PILOT
                )
        );

        flightDto = new FlightResponseDto(
                2L,
                "BA123",
                "JFK",
                "LAX",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                aircraftDto,
                crewSet
        );

        responseDto = new BookingResponseDto(
                99L,
                passengerDto,
                flightDto,
                booking.getBookingTime(),
                BookingStatus.BOOKED,
                "12A"
        );
    }

    @Test
    void testCreateBooking_success() {
        when(bookingRepository.existsByPassenger_IdAndFlight_Id(1L, 2L)).thenReturn(false);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(responseDto);

        BookingResponseDto result = bookingService.createBooking(requestDto);

        assertNotNull(result);
        assertEquals("12A", result.seatNumber());
        verify(notificationService).send(any(NotificationRequest.class));
    }

    @Test
    void testCreateBooking_duplicate() {
        when(bookingRepository.existsByPassenger_IdAndFlight_Id(1L, 2L)).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> bookingService.createBooking(requestDto));
    }

    @Test
    void testCreateBooking_passengerNotFound() {
        when(bookingRepository.existsByPassenger_IdAndFlight_Id(1L, 2L)).thenReturn(false);
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(requestDto));
    }

    @Test
    void testCreateBooking_flightNotFound() {
        when(bookingRepository.existsByPassenger_IdAndFlight_Id(1L, 2L)).thenReturn(false);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(requestDto));
    }

    @Test
    void testGetBookingById_found() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(responseDto);

        BookingResponseDto result = bookingService.getBookingById(99L);
        assertEquals(responseDto, result);
    }

    @Test
    void testGetBookingById_notFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(99L));
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(bookingMapper.toDtoList(List.of(booking))).thenReturn(List.of(responseDto));

        List<BookingResponseDto> result = bookingService.getAllBookings();
        assertEquals(1, result.size());
    }

    @Test
    void testGetBookingsByPassenger() {
        when(bookingRepository.findByPassenger_Id(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toDtoList(List.of(booking))).thenReturn(List.of(responseDto));

        List<BookingResponseDto> result = bookingService.getBookingsByPassenger(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetBookingsByFlight() {
        when(bookingRepository.findByFlight_Id(2L)).thenReturn(List.of(booking));
        when(bookingMapper.toDtoList(List.of(booking))).thenReturn(List.of(responseDto));

        List<BookingResponseDto> result = bookingService.getBookingsByFlight(2L);
        assertEquals(1, result.size());
    }

    @Test
    void testCancelBooking_success() {
        booking.setStatus(BookingStatus.BOOKED);
        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertDoesNotThrow(() -> bookingService.cancelBooking(99L));
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(notificationService).send(any(NotificationRequest.class));
    }

    @Test
    void testCancelBooking_notFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookingService.cancelBooking(99L));
    }
}
