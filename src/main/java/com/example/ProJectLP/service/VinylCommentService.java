package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylCommnet.VinylComment;
import com.example.ProJectLP.domain.vinylCommnet.VinylCommentRepository;
import com.example.ProJectLP.dto.request.VinylCommentRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylCommentResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VinylCommentService {

    private final TokenProvider tokenProvider;
    private final VinylRepository vinylRepository;
    private final VinylCommentRepository vinylCommentRepository;

    //vinyl 댓글 등록
    @Transactional
    public ResponseDto<?> uploadVinylComment(Long vinylId, VinylCommentRequestDto requestDto, HttpServletRequest request) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("400",
                    "Login is required.");
        }

        if (null == request.getHeader("Authorization")) {
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

        VinylComment vinylComment = VinylComment.builder()
                .vinyl(vinyl)
                .member(member)
                .content(requestDto.getContent())
                .build();

        vinylCommentRepository.save(vinylComment);

        return ResponseDto.success(
                VinylCommentResponseDto.builder()
                        .id(vinylComment.getId())
                        .username(vinylComment.getMember().getUsername())
                        .content(vinylComment.getContent())
                        .build()
        );


    }

    @Transactional
    public ResponseDto<?> deleteVinylComment(Long vinylId, Long id, HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("400",
                    "Login is required.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("400", "INVALID_TOKEN");
        }

        Vinyl vinyl = isPresentVinyl(vinylId);
        VinylComment vinylComment = isPresentVinylComment(id);
        if (null == vinyl || null == vinylComment) {
            return ResponseDto.fail("400", "Not existing vinylId or vinylCommentId");
        }

        if (vinylComment.validateMember(member)){

            if (member.isRole()){
                vinylCommentRepository.delete(vinylComment);
                return ResponseDto.success("Delete Success");
            }

            return ResponseDto.fail("400", "Delete Author Only");
        }

        vinylCommentRepository.delete(vinylComment);
        return ResponseDto.success("Delete Success");
    }

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
    public VinylComment isPresentVinylComment(Long id) {
        Optional<VinylComment> optionalVinylComment = vinylCommentRepository.findById(id);
        return optionalVinylComment.orElse(null);
    }
}
