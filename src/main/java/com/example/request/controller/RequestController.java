package com.example.request.controller;

import com.example.request.dto.RequestDto;
import com.example.request.dto.ResponseDto;
import com.example.request.entity.AuditLog;
import com.example.request.service.ApprovalService;
import com.example.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    ApprovalService approvalService;

    @PostMapping("/requests")
    public ResponseDto create(@RequestBody RequestDto requestDto) {
        return requestService.createRequest(requestDto);
    }

    @PostMapping("/requests/{id}/submit")
    public ResponseDto submit(@PathVariable Long id) {
        return requestService.submitRequest(id);
    }

    @PostMapping("/approvals/{stepId}/approve")
    public String approve(@PathVariable Long stepId) {
        return approvalService.approval(stepId);
    }

    @PostMapping("/approvals/{stepId}/reject")
    public String reject(@PathVariable Long stepId) {
        return approvalService.rejection(stepId);
    }

    @GetMapping("/requests/{id}/timeline")
    public List<AuditLog> timeline(@PathVariable Long id) {
        return requestService.timeline(id);
    }

}
