package com.example.ProJectLP.domain.vinyl;

import com.example.ProJectLP.domain.TimeStamped;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Vinyl extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String artist;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String releasedYear;

    @Column(nullable = false)
    private String releasedMonth;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @OneToMany(mappedBy = "vinyl", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Song> songs;

    @OneToMany(mappedBy = "vinyl", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VinylComment> vinylComments;

    @OneToMany(mappedBy = "vinyl", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VinylLike> vinylLikes;


}
