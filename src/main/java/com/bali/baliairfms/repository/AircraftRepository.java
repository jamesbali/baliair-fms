package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {


    Optional<Aircraft> findByTailNumber(String tailNumber);

    boolean existsByTailNumber(String tailNumber);
}
