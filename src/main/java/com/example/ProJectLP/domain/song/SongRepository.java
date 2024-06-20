package com.example.ProJectLP.domain.song;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {

    void deleteAllByVinylId(Long vinylId);

    List<Song> findAllByVinyl(Vinyl vinyl);
}
