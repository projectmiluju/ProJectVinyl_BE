package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;

    //vinyl 삭제
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

            String releaseYear = requestDto.getReleasedTime().substring(0,4);
            String releaseMonth = requestDto.getReleasedTime().substring(4,6);


            String imageUrl = s3Service.upload(multipartFile);

            Vinyl vinyl = Vinyl.builder()
                    .title(requestDto.getTitle())
                    .description(requestDto.getDescription())
                    .artist(requestDto.getArtist())
                    .genre(requestDto.getGenre())
                    .imageUrl(imageUrl)
                    .releasedYear(releaseYear)
                    .releasedMonth(releaseMonth)
                    .build();

            vinylRepository.save(vinyl);

            return ResponseDto.success(
                    VinylResponseDto.builder()
                            .id(vinyl.getId())
                            .title(vinyl.getTitle())
                            .description(vinyl.getDescription())
                            .artist(vinyl.getArtist())
                            .genre(vinyl.getGenre())
                            .imageUrl(vinyl.getImageUrl())
                            .releasedYear(releaseYear)
                            .releasedMonth(releaseMonth)
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
            return ResponseDto.fail("400", "Not existing postId");
        }

        if (!member.isRole()) {
            return ResponseDto.fail("400", "Deleted Admin Only");
        }

        vinylRepository.delete(vinyl);
        return ResponseDto.success("delete success");
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
