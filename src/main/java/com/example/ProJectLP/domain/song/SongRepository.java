package com.example.ProJectLP.domain.song;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

    void deleteAllByVinylId(Long vinylId);

    void findAllByVinylId(Long vinylId);
}
