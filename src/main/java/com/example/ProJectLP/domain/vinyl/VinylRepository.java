package com.example.ProJectLP.domain.vinyl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VinylRepository extends JpaRepository<Vinyl, Long> {

//    List<Vinyl> findAllByOrderByModifiedAtDesc();

    Page<Vinyl> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query(value = "SELECT b FROM Vinyl b WHERE b.title LIKE %:keyword%")
    Page<Vinyl> findByTitle(String keyword, Pageable pageable);
//    List<Vinyl> findByTitle(String keyword);


    @Modifying
    @Query("update Vinyl set view = view + 1 where id = :id")
    int updateView(Long id);
}
