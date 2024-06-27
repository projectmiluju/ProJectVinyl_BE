package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import com.example.ProJectLP.domain.vinylLike.VinylLikeRepository;
import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VinylLikeService {

    private final VinylLikeRepository vinylLikeRepository;
    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    //vinyl 좋아요 등록
    @Transactional
    public ResponseEntity<?> likeVinyl(Long vinylId, HttpServletRequest request){

        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }

        Vinyl vinyl = isPresentVinyl(vinylId);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        if (vinylLikeRepository.findByMemberIdAndVinylId(member.getId(),vinyl.getId()) != null) {
            throw new PrivateException(ErrorCode.VINYL_LIKE_ALREADY);
        }

        VinylLike vinylLike = VinylLike.builder()
                .vinyl(vinyl)
                .member(member)
                .build();
        vinylLikeRepository.save(vinylLike);

        return ResponseEntity.ok(Map.of("msg", "바이닐 좋아요가 완료 됐습니다." ));
    }

    //vinyl 좋아요 삭제
    @Transactional
    public ResponseEntity<?> unlikeVinyl(Long vinylId, HttpServletRequest request){
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }

        Vinyl vinyl = isPresentVinyl(vinylId);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }
        if (vinylLikeRepository.findByMemberIdAndVinylId(member.getId(),vinyl.getId()) == null) {
            throw new PrivateException(ErrorCode.VINYL_LIKE_NOT);
        }
        vinylLikeRepository.delete(vinylLikeRepository.findByMemberIdAndVinylId(member.getId(),vinyl.getId()));

        return ResponseEntity.ok(Map.of("msg","바이닐 좋아요 취소가 완료 됐습니다."));
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (refreshTokenService.getData(request.getHeader("RefreshToken")) == null) {
            return null;
        }
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
}
