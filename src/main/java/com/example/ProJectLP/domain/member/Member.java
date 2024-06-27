package com.example.ProJectLP.domain.member;

import com.example.ProJectLP.domain.TimeStamped;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String username;

    @Column(nullable = false,length = 50)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private boolean role;

    @Column
    private boolean emailCheck;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VinylComment> vinylComments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VinylLike> vinylLikes;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password){
        return passwordEncoder.matches(password, this.password);
    }
}
