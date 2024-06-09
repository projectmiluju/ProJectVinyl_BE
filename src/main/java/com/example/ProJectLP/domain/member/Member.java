package com.example.ProJectLP.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(unique = true, nullable = false, length = 10)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDateTime createdDate;

    @PrePersist
    public void createDate(){
        this.createdDate = LocalDateTime.now();
    }
}