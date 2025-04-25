package com.bali.baliairfms.model;

import com.bali.baliairfms.model.enums.CrewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "crew_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String staffId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String contactInfo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "crew_certifications", joinColumns = @JoinColumn(name = "crew_member_id"))
    @Column(name = "certification")
    private Set<String> certifications;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "crew_qualifications", joinColumns = @JoinColumn(name = "crew_member_id"))
    @Column(name = "qualification")
    private Set<String> qualifications;

    private boolean available;

    @Column(nullable = false)
    private int totalFlightHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrewType crewType;
}
