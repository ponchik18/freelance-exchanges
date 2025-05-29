package com.bsuir.entity;

import com.bsuir.enums.JobStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private String title;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private BigDecimal budget;
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    private String jobReference;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "skills_jobs",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private List<Skill> skills;
    @OneToMany(mappedBy = "job")
    private List<Proposal> proposals;

    public Job(long l) {
        this.id = l;
    }
}