package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> , JpaSpecificationExecutor<Flight> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

}
