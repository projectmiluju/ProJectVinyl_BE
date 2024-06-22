package com.example.ProJectLP.domain.vinylLike;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VinylLikeRepository extends JpaRepository<VinylLike, Long> {

    VinylLike findByMemberIdAndVinylId(Long memberId, Long vinylId);
}
