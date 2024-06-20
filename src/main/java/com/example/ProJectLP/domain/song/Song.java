package com.example.ProJectLP.domain.song;

import com.example.ProJectLP.domain.TimeStamped;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Song extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Character side;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String playingTime;

    @JoinColumn(name = "vinyl_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Vinyl vinyl;
}
