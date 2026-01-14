package com.example.request.dto;

import com.example.request.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDto {

    private Long id;
    private String title;
    private String description;
    private String requesterName;
    private Status status;
    private LocalDateTime createdAt;
}
