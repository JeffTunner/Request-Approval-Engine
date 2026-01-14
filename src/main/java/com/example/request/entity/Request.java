package com.example.request.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String requesterName;
    private enum status {DRAFT, SUBMITTED, APPROVED, REJECTED};

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<ApprovalStep> steps;
}
