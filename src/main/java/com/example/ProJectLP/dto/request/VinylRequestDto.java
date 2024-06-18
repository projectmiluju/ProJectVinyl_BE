package com.example.ProJectLP.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VinylRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    @NotBlank
    private String genre;

    @NotBlank
    private String description;

    @NotBlank
    private String releasedTime;
}
