package com.nvc.spring_boot.entity;

import com.nvc.spring_boot.util.constant.ResumeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name ="resumes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist()
    public void handleBeforePersist() {
        this.createdAt = Instant.now();
    }

    @PreUpdate()
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
