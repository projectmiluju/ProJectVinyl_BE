package com.example.ProJectLP.domain.vinyl;

import com.example.ProJectLP.domain.TimeStamped;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
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

    @OneToMany(mappedBy = "vinyl", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Song> songs;

    @OneToMany(mappedBy = "vinyl", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VinylComment> vinylComments;


    //추가되어야할 항목
    //좋아요
    //댓글

}
