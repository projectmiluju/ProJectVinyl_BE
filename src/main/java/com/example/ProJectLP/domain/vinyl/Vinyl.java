package com.example.ProJectLP.domain.vinyl;

import com.example.ProJectLP.domain.TimeStamped;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String releasedYear;

    @Column(nullable = false)
    private String releasedMonth;

    //추가되어야할 항목
    //좋아요
    //댓글

}
