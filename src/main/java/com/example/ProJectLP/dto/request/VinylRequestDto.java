package com.example.ProJectLP.dto.request;

import com.example.ProJectLP.domain.song.Song;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @NotBlank
    private List<Song> songs;
}
