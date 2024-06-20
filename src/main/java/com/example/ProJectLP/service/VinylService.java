package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.song.SongRepository;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylListResponseDto;
import com.example.ProJectLP.dto.response.VinylResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;
    private final SongRepository songRepository;

    //vinyl 등록
    @Transactional
    public ResponseDto<?> uploadVinyl(VinylRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
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

        if (member.isRole()){

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

            return ResponseDto.success(
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
                            .build()
            );
        }
        else return ResponseDto.fail("400", "Upload Admin Only");

    }

    //vinyl 삭제
    @Transactional
    public ResponseDto<?> deleteVinyl(Long id, HttpServletRequest request){
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

        Vinyl vinyl = isPresentVinyl(id);
        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        if (!member.isRole()) {
            return ResponseDto.fail("400", "Deleted Admin Only");
        }

        s3Service.delete(vinyl);
        vinylRepository.delete(vinyl);
        return ResponseDto.success("delete success");
    }

    //vinyl 업데이트
    @Transactional
    public ResponseDto<?> updateVinyl(Long id, VinylRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
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

        Vinyl vinyl = isPresentVinyl(id);
        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        if (!member.isRole()) {
            return ResponseDto.fail("400", "Update Admin Only");
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

        return ResponseDto.success(
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
                        .build()
        );
    }

    //vinyl 전체조회
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseDto<?> getVinylList() {
        List<Vinyl> allByOrderByModifiedAtDesc = vinylRepository.findAllByOrderByModifiedAtDesc();
        List<VinylListResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : allByOrderByModifiedAtDesc) {
            VinylListResponseDto vinylListResponseDto = new VinylListResponseDto(vinyl);
            dtoList.add(vinylListResponseDto);
        }
        return ResponseDto.success(dtoList);
    }

    //vinyl 상세조회
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseDto<?> getVinyl(Long id) {
        Vinyl vinyl = vinylRepository.findById(id).orElse(null);
        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        return ResponseDto.success(
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
                        .build()
        );
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
}
