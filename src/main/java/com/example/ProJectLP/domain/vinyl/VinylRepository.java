package com.example.ProJectLP.domain.vinyl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    List<Vinyl> findAllByOrderByModifiedAtDesc();
}
