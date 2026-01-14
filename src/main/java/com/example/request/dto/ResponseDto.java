package com.example.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDto {

    private Long id;
    private String title;
    private String description;
    private String requesterName;
    private enum status {DRAFT, SUBMITTED, APPROVED, REJECTED};
}
