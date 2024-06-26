package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.song.SongRepository;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylComment.VinylComment;
import com.example.ProJectLP.domain.vinylComment.VinylCommentRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.*;
import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;
    private final SongRepository songRepository;
    private final VinylCommentRepository vinylCommentRepository;
    private final RefreshTokenService refreshTokenService;

    private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    //vinyl 등록
    @Transactional
    public ResponseEntity<?> uploadVinyl(VinylRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }

        if (!member.isRole()){
            throw new PrivateException(ErrorCode.VINYL_UPLOAD_FORBIDDEN);
        }

        String releasedYear = requestDto.getReleasedTime().substring(0,4);
        String releasedMonth = requestDto.getReleasedTime().substring(4,6);

        String imageUrl = s3Service.upload(multipartFile);

        Vinyl vinyl = Vinyl.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .artist(requestDto.getArtist())
                .genre(requestDto.getGenre())
                .imageUrl(imageUrl)
                .releasedYear(releasedYear)
                .releasedMonth(releasedMonth)
                .build();
        vinylRepository.save(vinyl);

        for (int i = 0; i < requestDto.getSongs().size(); i++) {
            Song song = Song.builder()
                    .title(requestDto.getSongs().get(i).getTitle())
                    .side(requestDto.getSongs().get(i).getSide())
                    .playingTime(requestDto.getSongs().get(i).getPlayingTime())
                    .vinyl(vinyl).build();

            songRepository.save(song);
        }

        VinylResponseDto.builder()
                .id(vinyl.getId())
                .title(vinyl.getTitle())
                .description(vinyl.getDescription())
                .artist(vinyl.getArtist())
                .genre(vinyl.getGenre())
                .imageUrl(vinyl.getImageUrl())
                .releasedYear(releasedYear)
                .releasedMonth(releasedMonth)
                .createdAt(vinyl.getCreatedAt())
                .modifiedAt(vinyl.getModifiedAt())
                .build();

        return ResponseEntity.ok(Map.of("msg", "바이닐 등록이 완료 됐습니다.", "data", vinyl.getId()));
    }

    //vinyl 삭제
    @Transactional
    public ResponseEntity<?> deleteVinyl(Long id, HttpServletRequest request){
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }

        if (!member.isRole()) {
            throw new PrivateException(ErrorCode.VINYL_DELETE_FORBIDDEN);
        }

        Vinyl vinyl = isPresentVinyl(id);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        s3Service.delete(vinyl);
        vinylRepository.delete(vinyl);

        return ResponseEntity.ok(Map.of("msg", "바이닐 삭제가 완료 됐습니다.", "data", vinyl.getId()));
    }

    //vinyl 업데이트
    @Transactional
    public ResponseEntity<?> updateVinyl(Long id, VinylRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }
        if (!member.isRole()) {
            throw new PrivateException(ErrorCode.VINYL_MODIFY_FORBIDDEN);
        }

        Vinyl vinyl = isPresentVinyl(id);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            vinyl.setTitle(vinyl.getTitle());
        }else {
            vinyl.setTitle(requestDto.getTitle());
        }

        if (requestDto.getDescription() == null || requestDto.getDescription().isEmpty()) {
            vinyl.setDescription(vinyl.getDescription());
        }else {
            vinyl.setDescription(requestDto.getDescription());
        }

        if (requestDto.getArtist() == null || requestDto.getArtist().isEmpty()) {
            vinyl.setArtist(vinyl.getArtist());
        }else {
            vinyl.setArtist(requestDto.getArtist());
        }

        if (requestDto.getGenre() == null || requestDto.getGenre().isEmpty()) {
            vinyl.setGenre(vinyl.getGenre());
        }else {
            vinyl.setGenre(requestDto.getGenre());
        }

        if (multipartFile.isEmpty()){
            vinyl.setImageUrl(vinyl.getImageUrl());
        }else {
            s3Service.delete(vinyl);
            vinyl.setImageUrl(s3Service.upload(multipartFile));
        }

        if (requestDto.getReleasedTime() == null || requestDto.getReleasedTime().isEmpty()) {
            vinyl.setReleasedYear(vinyl.getReleasedYear());
            vinyl.setReleasedMonth(vinyl.getReleasedMonth());
        }else {
            String releasedYear = requestDto.getReleasedTime().substring(0,4);
            String releasedMonth = requestDto.getReleasedTime().substring(4,6);

            vinyl.setReleasedYear(releasedYear);
            vinyl.setReleasedMonth(releasedMonth);
        }

        vinyl.setSongs(vinyl.getSongs());

        VinylResponseDto.builder()
                .id(vinyl.getId())
                .title(vinyl.getTitle())
                .description(vinyl.getDescription())
                .artist(vinyl.getArtist())
                .genre(vinyl.getGenre())
                .imageUrl(vinyl.getImageUrl())
                .releasedYear(vinyl.getReleasedYear())
                .releasedMonth(vinyl.getReleasedMonth())
                .createdAt(vinyl.getCreatedAt())
                .modifiedAt(vinyl.getModifiedAt())
                .build();

        return ResponseEntity.ok(Map.of("msg", "바이닐 수정이 완료 됐습니다.", "data", vinyl.getId()));
    }

    //vinyl 전체조회
    @Transactional
    public ResponseEntity<?> getVinylList(int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit);

        Page<Vinyl> allByOrderByModifiedAtDesc = vinylRepository.findAllByOrderByModifiedAtDesc(pageable);
        List<VinylListResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : allByOrderByModifiedAtDesc) {
            VinylListResponseDto vinylListResponseDto = new VinylListResponseDto(vinyl);
            dtoList.add(vinylListResponseDto);
        }

        PageVinylResponseDto pageVinylResponseDto = PageVinylResponseDto.builder()
                .currPage(allByOrderByModifiedAtDesc.getNumber()+1)
                .totalPage(allByOrderByModifiedAtDesc.getTotalPages())
                .currContent(allByOrderByModifiedAtDesc.getNumberOfElements())
                .vinylList(dtoList)
                .build();

        return ResponseEntity.ok(Map.of("msg", "바이닐 조회가 완료 됐습니다.", "data", pageVinylResponseDto));
    }

    //vinyl 상세조회
    @Transactional
    public ResponseEntity<?> getVinyl(Long id) {
        Vinyl vinyl = vinylRepository.findById(id).orElse(null);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        List<Song> songs = songRepository.findAllByVinyl(vinyl);
        List<SongResponseDto> songResponseDtoList = new ArrayList<>();
        for (Song song : songs) {
            SongResponseDto songResponseDto = SongResponseDto.builder()
                    .id(song.getId())
                    .side(song.getSide())
                    .title(song.getTitle())
                    .playingTime(song.getPlayingTime())
                    .build();
            songResponseDtoList.add(songResponseDto);
        }

        List<VinylComment> vinylComments = vinylCommentRepository.findTop3ByVinylOrderByCreatedAtDesc(vinyl);
        List<VinylCommentResponseDto> vinylCommentResponseDtoList = new ArrayList<>();
        for (VinylComment vinylComment : vinylComments) {
            VinylCommentResponseDto vinylCommentResponseDto = VinylCommentResponseDto.builder()
                    .id(vinylComment.getId())
                    .username(vinylComment.getMember().getUsername())
                    .content(vinylComment.getContent())
                    .build();
            vinylCommentResponseDtoList.add(vinylCommentResponseDto);
        }

        VinylResponseDto vinylResponseDto = VinylResponseDto.builder()
                .id(vinyl.getId())
                .title(vinyl.getTitle())
                .description(vinyl.getDescription())
                .artist(vinyl.getArtist())
                .genre(vinyl.getGenre())
                .imageUrl(vinyl.getImageUrl())
                .releasedYear(vinyl.getReleasedYear())
                .releasedMonth(vinyl.getReleasedMonth())
                .numComments(vinyl.getVinylComments().size())
                .numLikes(vinyl.getVinylLikes().size())
                .numView(vinyl.getView())
                .songs(songResponseDtoList)
                .vinylComments(vinylCommentResponseDtoList)
                .createdAt(vinyl.getCreatedAt())
                .modifiedAt(vinyl.getModifiedAt())
                .build();

        return ResponseEntity.ok(Map.of("msg", "바이닐 상세 조회가 완료 됐습니다.", "data", vinylResponseDto));

    }

    @Transactional
    public int updateView(Long id, HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        boolean checKCookie = false;
        int result = 0;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(VIEWCOOKIENAME+id)) {
                    checKCookie = true;
                }
            }
            if (!checKCookie) {
                Cookie newCookie = createCookieForForNotOverlap(id);
                response.addCookie(newCookie);
                result = vinylRepository.updateView(id);
            }
        }else {
            Cookie newCookie = createCookieForForNotOverlap(id);
            response.addCookie(newCookie);
            result = vinylRepository.updateView(id);
        }
        return result;
    }

    /*
     * 조회수 중복 방지를 위한 쿠키 생성 메소드
     * @param cookie
     * @return
     * */
    private Cookie createCookieForForNotOverlap(Long postId) {
        Cookie cookie = new Cookie(VIEWCOOKIENAME+postId, String.valueOf(postId));
        cookie.setComment("조회수 중복 증가 방지 쿠키");// 쿠키 용도 설명 기재
        cookie.setMaxAge(getRemainSecondForTomorrow()); 	// 하루를 준다.
        cookie.setHttpOnly(true);				// 서버에서만 조작 가능
        return cookie;
    }

    // 다음 날 정각까지 남은 시간(초)
    private int getRemainSecondForTomorrow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tomorrow, ChronoUnit.SECONDS);
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
