package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.song.SongRepository;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.SongRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;
    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;

    //트랙리스트 수정
    @Transactional
        public ResponseDto<?> updateSong(Long vinylid, SongRequestDto songs, HttpServletRequest request) {
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

        Vinyl vinyl = isPresentVinyl(vinylid);
        if (null == vinyl) {
            return ResponseDto.fail("400", "Not existing vinylId");
        }

        if (!member.isRole()) {
            return ResponseDto.fail("400", "Update Admin Only");
        }

        songRepository.deleteAllByVinylId(vinyl.getId());


        for (int i = 0; i < songs.getSongs().size(); i++) {
            Song song = Song.builder()
                    .title(songs.getSongs().get(i).getTitle())
                    .side(songs.getSongs().get(i).getSide())
                    .playingTime(songs.getSongs().get(i).getPlayingTime())
                    .vinyl(vinyl)
                    .build();

            songRepository.save(song);


        }

        return ResponseDto.success("update trackList");
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
