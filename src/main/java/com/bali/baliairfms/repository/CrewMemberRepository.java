package com.bali.baliairfms.repository;

import com.bali.baliairfms.model.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

    Optional<CrewMember> findByStaffId(String staffId);

    boolean existsByStaffId(String staffId);


}
