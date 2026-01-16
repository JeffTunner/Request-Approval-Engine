package com.example.request.service;

import com.example.request.entity.*;
import com.example.request.repository.ApprovalRepository;
import com.example.request.repository.AuditLogRepository;
import com.example.request.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalService {

    @Autowired
    ApprovalRepository approvalRepository;

    @Autowired
    AuditLogRepository logRepository;

    @Autowired
    RequestRepository requestRepository;

    public String approval(Long stepId) {
        ApprovalStep approvalStep = approvalRepository.findById(stepId).orElseThrow(() -> new RuntimeException("Wrong step id!"));
        Request request = approvalStep.getRequest();
        if(request.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Request already Rejected");
        }
        if(request.getStatus() == Status.APPROVED) {
            throw new IllegalStateException("Request Already Approved!");
        }
        if(approvalStep.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Step already processed");
        }
        List<ApprovalStep> steps = approvalRepository.findByRequestId(request.getId());
        int minPendingOrder = steps.stream().filter(s -> s.getStatus() == Status.PENDING).mapToInt(ApprovalStep::getApprovalOrder).min().orElseThrow(() -> new IllegalStateException("No Pending Steps"));
        if(approvalStep.getApprovalOrder() != minPendingOrder) {
            throw new IllegalStateException("Cannot Approve out of Order");
        }
        approvalStep.setStatus(Status.APPROVED);
        approvalRepository.save(approvalStep);
        boolean allApproved = steps.stream().allMatch(s -> s.getStatus() == Status.APPROVED || s.getId().equals(approvalStep.getId()));
        if(allApproved) {
            request.setStatus(Status.APPROVED);
            requestRepository.save(request);
        }
        AuditLog auditLog = new AuditLog();
        auditLog.setRequestId(request.getId());
        auditLog.setModifiedAt(LocalDateTime.now());
        auditLog.setName(request.getRequesterName());
        auditLog.setCreatedAt(request.getCreatedAt());
        auditLog.setStatus(Status.APPROVED);
        auditLog.setRole(approvalStep.getRole());
        logRepository.save(auditLog);
        return approvalStep.getRole() + " Approved your Request";
    }

    public String rejection(Long stepId) {
        ApprovalStep approvalStep = approvalRepository.findById(stepId).orElseThrow(() -> new RuntimeException("Wrong step id!"));
        Request request = approvalStep.getRequest();
        if(request.getStatus() == Status.APPROVED) {
            throw new IllegalStateException("Request Already Approved!");
        }
        if(request.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Cannot reject again, already Rejected!");
        }
        approvalStep.setStatus(Status.REJECTED);
        approvalRepository.save(approvalStep);
        request.setStatus(Status.REJECTED);
        requestRepository.save(request);
        AuditLog auditLog = new AuditLog();
        auditLog.setRequestId(request.getId());
        auditLog.setModifiedAt(LocalDateTime.now());
        auditLog.setName(request.getRequesterName());
        auditLog.setCreatedAt(request.getCreatedAt());
        auditLog.setStatus(Status.REJECTED);
        auditLog.setRole(approvalStep.getRole());
        logRepository.save(auditLog);
        return "Request Rejected!!!";
    }
}
