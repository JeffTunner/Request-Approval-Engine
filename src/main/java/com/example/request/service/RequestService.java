package com.example.request.service;

import com.example.request.dto.RequestDto;
import com.example.request.dto.ResponseDto;
import com.example.request.entity.*;
import com.example.request.repository.ApprovalRepository;
import com.example.request.repository.AuditLogRepository;
import com.example.request.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.request.entity.Status.DRAFT;
import static com.example.request.entity.Status.SUBMITTED;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    AuditLogRepository logRepository;

    @Autowired
    ApprovalRepository approvalRepository;

    // DTO -> Entity
    private Request toEntity(RequestDto dto) {
        Request request = new Request();
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setRequesterName(dto.getRequesterName());
        return request;
    }

    // Entity -> DTO
    private ResponseDto toDto(Request request) {
        return new ResponseDto(request.getId(), request.getTitle(), request.getDescription(), request.getRequesterName(), request.getStatus(), request.getCreatedAt());
    }

    public ResponseDto createRequest(RequestDto requestDto) {
        Request request = toEntity(requestDto);
        request.setStatus(DRAFT);
        request.setCreatedAt(LocalDateTime.now());
        Request saved = requestRepository.save(request);
        AuditLog auditLog = new AuditLog();
        auditLog.setRequestId(request.getId());
        auditLog.setName(requestDto.getRequesterName());
        auditLog.setStatus(DRAFT);
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setModifiedAt(LocalDateTime.now());
        logRepository.save(auditLog);
        return toDto(saved);
    }

    public ResponseDto submitRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request with id: " +id + " not found!"));
        if(request.getStatus() != DRAFT) {
            throw new IllegalStateException("Only drafts can be submitted!");
        }
        request.setStatus(SUBMITTED);
        requestRepository.save(request);
        AuditLog auditLog = new AuditLog();
        auditLog.setRequestId(request.getId());
        auditLog.setName(request.getRequesterName());
        auditLog.setStatus(SUBMITTED);
        auditLog.setCreatedAt(request.getCreatedAt());
        auditLog.setModifiedAt(LocalDateTime.now());
        logRepository.save(auditLog);
        ApprovalStep approvalStep = new ApprovalStep();
        approvalStep.setRequest(request);
        approvalStep.setRole(Roles.MANAGER);
        approvalStep.setStatus(Status.PENDING);
        approvalStep.setApprovalOrder(0);
        approvalRepository.save(approvalStep);
        ApprovalStep approvalStep1 = new ApprovalStep();
        approvalStep1.setRequest(request);
        approvalStep1.setRole(Roles.HR);
        approvalStep1.setStatus(Status.PENDING);
        approvalStep1.setApprovalOrder(1);
        approvalRepository.save(approvalStep1);
        ApprovalStep approvalStep2 = new ApprovalStep();
        approvalStep2.setRequest(request);
        approvalStep2.setRole(Roles.FINANCE);
        approvalStep2.setStatus(Status.PENDING);
        approvalStep2.setApprovalOrder(2);
        approvalRepository.save(approvalStep2);
        return toDto(request);
    }

    public List<AuditLog> timeline(Long id) {
        return logRepository.findByRequestId(id);
    }
}
