package com.example.ProJectLP.domain.vinyl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    List<Vinyl> findAllByOrderByModifiedAtDesc();

    @Query(value = "SELECT b FROM Vinyl b WHERE b.title LIKE %:keyword%")
    List<Vinyl> findByTitle(String keyword);

    List<Vinyl> findTop10ByOrderByVinylLikesAsc();
}
