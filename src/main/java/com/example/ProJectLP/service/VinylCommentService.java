package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylComment.VinylCommentRepository;
import com.example.ProJectLP.dto.request.VinylCommentRequestDto;
import com.example.ProJectLP.dto.response.PageVinylCommentResponseDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylCommentResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    //vinyl 댓글 삭제
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

    //vinyl 댓글 수정
    @Transactional
    public ResponseDto<?> updateVinylComment(Long vinylId, Long id, VinylCommentRequestDto requestDto, HttpServletRequest request) {
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
            return ResponseDto.fail("400", "Update Author Only");
        }

        vinylComment.update(requestDto);
        return ResponseDto.success(
                VinylCommentResponseDto.builder()
                        .id(vinylComment.getId())
                        .username(vinylComment.getMember().getUsername())
                        .content(vinylComment.getContent())
                        .build()
        );
    }

    //vinyl 댓글 조회
    @Transactional
    public ResponseDto<?> getVinylCommentList(Long vinylId, int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit);

        Vinyl vinyl = vinylRepository.findById(vinylId).orElse(null);
        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        Page<VinylComment> vinylComments = vinylCommentRepository.findByVinylOrderByCreatedAtDesc(vinyl,pageable);
        if (vinylComments.isEmpty()) {
            return ResponseDto.fail("400", "Not existing vinylComment");
        }
        List<VinylCommentResponseDto> vinylCommentResponseDtoList = new ArrayList<>();
        for (VinylComment vinylComment : vinylComments) {
            VinylCommentResponseDto vinylCommentResponseDto = VinylCommentResponseDto.builder()
                    .id(vinylComment.getId())
                    .username(vinylComment.getMember().getUsername())
                    .content(vinylComment.getContent())
                    .build();
            vinylCommentResponseDtoList.add(vinylCommentResponseDto);
        }

        PageVinylCommentResponseDto pageVinylCommentResponseDto = PageVinylCommentResponseDto.builder()
                .currPage(vinylComments.getNumber()+1)
                .totalPage(vinylComments.getTotalPages())
                .currContent(vinylComments.getNumberOfElements())
                .vinylCommentList(vinylCommentResponseDtoList).build();

        return ResponseDto.success(pageVinylCommentResponseDto);
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
