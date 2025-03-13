package com.nvc.spring_boot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvc.spring_boot.util.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resume> resumes;

    @PrePersist()
    public void handleBeforePersist() {
        this.createdAt = Instant.now();
    }

    @PreUpdate()
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
