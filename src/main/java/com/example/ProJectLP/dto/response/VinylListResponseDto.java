package com.example.ProJectLP.dto.response;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VinylListResponseDto {
    private Long id;
    private String title;
    private String description;
    private String artist;
    private String genre;
    private String releasedYear;
    private String releasedMonth;
    private String imageUrl;
    private Integer numComments;
    private Integer numLikes;
    private Integer numView;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public VinylListResponseDto(Vinyl vinyl) {
        this.id = vinyl.getId();
        this.title = vinyl.getTitle();
        this.description = vinyl.getDescription();
        this.artist = vinyl.getArtist();
        this.genre = vinyl.getGenre();
        this.releasedYear = vinyl.getReleasedYear();
        this.releasedMonth = vinyl.getReleasedMonth();
        this.imageUrl = vinyl.getImageUrl();
        this.numComments = vinyl.getVinylComments().size();
        this.numLikes = vinyl.getVinylLikes().size();
        this.numView = vinyl.getView();
        this.createdAt = vinyl.getCreatedAt();
        this.modifiedAt = vinyl.getModifiedAt();
    }
}
