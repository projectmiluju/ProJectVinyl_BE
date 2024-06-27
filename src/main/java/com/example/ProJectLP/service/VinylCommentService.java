package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylComment.VinylCommentRepository;
import com.example.ProJectLP.dto.request.VinylCommentRequestDto;
import com.example.ProJectLP.dto.response.PageVinylCommentResponseDto;
import com.example.ProJectLP.dto.response.VinylCommentResponseDto;

import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VinylCommentService {

    private final TokenProvider tokenProvider;
    private final VinylRepository vinylRepository;
    private final VinylCommentRepository vinylCommentRepository;
    private final RefreshTokenService refreshTokenService;

    //vinyl 댓글 등록
    @Transactional
    public ResponseEntity<?> uploadVinylComment(Long vinylId, VinylCommentRequestDto requestDto, HttpServletRequest request) {

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

        if (requestDto.getContent().isBlank()){
            throw new PrivateException(ErrorCode.VINYL_COMMENT_EMPTY_CONTENT);
        }

        VinylComment vinylComment = VinylComment.builder()
                .vinyl(vinyl)
                .member(member)
                .content(requestDto.getContent())
                .build();
        vinylCommentRepository.save(vinylComment);

        return ResponseEntity.ok(Map.of("msg", "댓글 등록이 완료 됐습니다.","data", vinyl.getId()));
    }

    //vinyl 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteVinylComment(Long vinylId, Long id, HttpServletRequest request) {
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
        VinylComment vinylComment = isPresentVinylComment(id);
        if (null == vinylComment) {
            throw new PrivateException(ErrorCode.VINYL_COMMENT_NOTFOUND);
        }

        if (!vinylComment.getMember().equals(member)) {
            if (member.isRole()){
                vinylCommentRepository.delete(vinylComment);
                return ResponseEntity.ok(Map.of("msg", "바이닐 댓글 삭제가 완료 됐습니다.","data", vinyl.getId()));
            }
            throw new PrivateException(ErrorCode.VINYL_COMMENT_DELETE_FORBIDDEN);
        }

        vinylCommentRepository.delete(vinylComment);
        return ResponseEntity.ok(Map.of("msg", "바이닐 댓글 삭제기 완료 됐습니다.","data", vinyl.getId()));
    }

    //vinyl 댓글 수정
    @Transactional
    public ResponseEntity<?> updateVinylComment(Long vinylId, Long id, VinylCommentRequestDto requestDto, HttpServletRequest request) {
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
        VinylComment vinylComment = isPresentVinylComment(id);
        if (null == vinylComment) {
            throw new PrivateException(ErrorCode.VINYL_COMMENT_NOTFOUND);
        }

        if (requestDto.getContent().isBlank()){
            throw new PrivateException(ErrorCode.VINYL_COMMENT_EMPTY_CONTENT);
        }

        if (!vinylComment.getMember().equals(member)) {
            if (member.isRole()){
                vinylComment.update(requestDto);
                return ResponseEntity.ok(Map.of("msg", "바이닐 댓글 수정이 완료 됐습니다.","data", vinyl.getId()));
            }
            throw new PrivateException(ErrorCode.VINYL_COMMENT_MODIFY_FORBIDDEN);
        }

        vinylComment.update(requestDto);
        return ResponseEntity.ok(Map.of("msg", "바이닐 댓글 수정이 완료 됐습니다.","data", vinyl.getId()));
    }

    //vinyl 댓글 전체 조회
    @Transactional
    public ResponseEntity<?> getVinylCommentList(Long vinylId, int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit);

        Vinyl vinyl = isPresentVinyl(vinylId);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        Page<VinylComment> vinylComments = vinylCommentRepository.findByVinylOrderByCreatedAtDesc(vinyl,pageable);
        if (vinylComments.isEmpty()) {
            throw new PrivateException(ErrorCode.VINYL_COMMENT_EMPTY);
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

        return ResponseEntity.ok(Map.of("msg", "바이닐 댓글 전체 조회가 완료 됐습니다.", "data", pageVinylCommentResponseDto));
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

    @Transactional
    public VinylComment isPresentVinylComment(Long id) {
        Optional<VinylComment> optionalVinylComment = vinylCommentRepository.findById(id);
        return optionalVinylComment.orElse(null);
    }
}
