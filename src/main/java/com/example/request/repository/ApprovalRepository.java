package com.example.request.repository;

import com.example.request.entity.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalStep, Long> {

    List<ApprovalStep> findByRequestId(Long id);
}
