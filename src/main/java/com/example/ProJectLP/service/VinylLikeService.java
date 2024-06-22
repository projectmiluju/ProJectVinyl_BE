package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import com.example.ProJectLP.domain.vinylLike.VinylLikeRepository;
import com.example.ProJectLP.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VinylLikeService {

    private final VinylLikeRepository vinylLikeRepository;
    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;

    //vinyl 좋아요 등록
    @Transactional
    public ResponseDto<?> likeVinyl(Long vinylId, HttpServletRequest request){

        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("400",
                    "Login is required.");
        }

        Member member = validateMember(request);

        if (null == member) {
            return ResponseDto.fail("400", "INVALID_TOKEN");
        }

        Vinyl vinyl = isPresentVinyl(vinylId);

        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        if (vinylLikeRepository.findByMemberIdAndVinylId(member.getId(),vinyl.getId()) != null) {
            return ResponseDto.fail("400", "Already liked vinylId");
        }

        VinylLike vinylLike = VinylLike.builder()
                .vinyl(vinyl)
                .member(member)
                .build();
        vinylLikeRepository.save(vinylLike);

        return ResponseDto.success("Successfully liked vinyl");
    }

//    @Transactional
//    public ResponseDto<?> unlikeVinyl(Long vinylId, HttpServletRequest request){
//
//    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional
    public Vinyl isPresentVinyl(Long id) {
        Optional<Vinyl> optionalVinyl = vinylRepository.findById(id);
        return optionalVinyl.orElse(null);
    }

    @Transactional
    public VinylLike isPresentVinylLike(Long id) {
        Optional<VinylLike> optionalVinylLike = vinylLikeRepository.findById(id);
        return optionalVinylLike.orElse(null);
    }
}
