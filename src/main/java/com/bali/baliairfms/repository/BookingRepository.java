package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPassenger_Id(Long passengerId);

    List<Booking> findByFlight_Id(Long flightId);

    Optional<Booking> findByPassenger_IdAndFlight_Id(Long passengerId, Long flightId);

    boolean existsByPassenger_IdAndFlight_Id(Long passengerId, Long flightId);



}
