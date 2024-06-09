package com.example.ProJectLP.domain.vinyl;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Vinyl {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

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






}
