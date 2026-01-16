package com.example.request.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Roles role;
    private Long requestId;
}
