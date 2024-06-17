package com.example.ProJectLP.domain.member;

import com.example.ProJectLP.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(unique = true, nullable = false, length = 10)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean role;

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password){
        return passwordEncoder.matches(password, this.password);
    }
}
