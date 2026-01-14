package com.example.request.service;

import com.example.request.dto.RequestDto;
import com.example.request.dto.ResponseDto;
import com.example.request.entity.AuditLog;
import com.example.request.entity.Request;
import com.example.request.repository.ApprovalRepository;
import com.example.request.repository.AuditLogRepository;
import com.example.request.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        auditLog.setName(requestDto.getRequesterName());
        auditLog.setStatus(DRAFT);
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setModifiedAt(LocalDateTime.now());
        logRepository.save(auditLog);
        return toDto(saved);
    }

    public ResponseDto submitRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request with id: " +id + " not found!"));
        request.setStatus(SUBMITTED);
        AuditLog auditLog = new AuditLog();
        auditLog.setName(request.getRequesterName());
        auditLog.setStatus(SUBMITTED);
        auditLog.setCreatedAt(request.getCreatedAt());
        auditLog.setModifiedAt(LocalDateTime.now());
        logRepository.save(auditLog);
        return toDto(request);
    }
}
