package com.example.ProJectLP.domain.vinylLike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VinylLikeRepository extends JpaRepository<VinylLike, Long> {

    VinylLike findByMemberIdAndVinylId(Long memberId, Long vinylId);
}
