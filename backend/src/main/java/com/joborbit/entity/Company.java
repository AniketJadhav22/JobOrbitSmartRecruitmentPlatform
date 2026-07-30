package com.joborbit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Company profile owned by a RECRUITER user.
 */
@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recruiter_id", nullable = false, unique = true)
    private User recruiter;

    @Column(nullable = false)
    private String name;

    private String website;

    @Column(length = 2000)
    private String description;

    private String location;
}
