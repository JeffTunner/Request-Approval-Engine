package com.example.request.controller;

import com.example.request.dto.RequestDto;
import com.example.request.dto.ResponseDto;
import com.example.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestController {

    @Autowired
    RequestService requestService;

    @PostMapping("/requests")
    public ResponseDto create(@RequestBody RequestDto requestDto) {
        return requestService.createRequest(requestDto);
    }

    @PostMapping("/requests/{id}/submit")
    public ResponseDto submit(@PathVariable Long id) {
        return requestService.submitRequest(id);
    }

}
