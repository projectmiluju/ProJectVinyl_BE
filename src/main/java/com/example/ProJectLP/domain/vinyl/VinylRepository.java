package com.example.ProJectLP.domain.vinyl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VinylRepository extends JpaRepository<Vinyl, Long> {

//    List<Vinyl> findAllByOrderByModifiedAtDesc();

    Page<Vinyl> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query(value = "SELECT b FROM Vinyl b WHERE b.title LIKE %:keyword%")
    List<Vinyl> findByTitle(String keyword);

}
