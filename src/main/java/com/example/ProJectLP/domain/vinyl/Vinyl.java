package com.example.ProJectLP.domain.vinyl;

import com.example.ProJectLP.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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

    @Column
    private String imageUrl;

    @Column
    private Long releaseYear;

    //추가되어야할 항목
    //좋아요
    //댓글

}
