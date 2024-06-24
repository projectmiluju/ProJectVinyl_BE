package com.example.ProJectLP.domain.vinylLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VinylLikeRepository extends JpaRepository<VinylLike, Long> {

    VinylLike findByMemberIdAndVinylId(Long memberId, Long vinylId);

//    @Query(value = "SELECT * FROM vinyl_like WHERE created_at >= DATE_ADD(NOW(), INTERVAL -1 MONTH ) AND created_at <=NOW()", nativeQuery = true)
    @Query(value = "SELECT * FROM vinyl_like WHERE vinyl_id = :vinylId AND (created_at >= DATE_ADD(NOW(), INTERVAL -1 MONTH ) AND created_at <=NOW())", nativeQuery = true)
    List<VinylLike> findByVinylId(Long vinylId);
}

