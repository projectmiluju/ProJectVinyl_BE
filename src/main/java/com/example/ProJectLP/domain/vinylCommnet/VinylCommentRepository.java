package com.example.ProJectLP.domain.vinylCommnet;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinylCommentRepository extends JpaRepository<VinylComment, Long> {

    List<VinylComment> findTop3ByVinylOrderByCreatedAtDesc(Vinyl vinyl);
}
