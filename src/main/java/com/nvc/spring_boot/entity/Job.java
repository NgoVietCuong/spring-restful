package com.nvc.spring_boot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nvc.spring_boot.util.constant.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="jobs")
@Data
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job name must not be empty")
    private String name;

    private String location;
    private Double salary;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany()
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    List<Skill> skills;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
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
