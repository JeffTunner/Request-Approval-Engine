package com.example.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {

    @NotBlank(message = "Title should not be blank!")
    private String title;

    @NotBlank(message = "Description should not be blank!")
    private String description;

    @NotBlank(message = "Name should not be blank!")
    private String requesterName;
}
