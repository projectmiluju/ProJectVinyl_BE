package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.song.Song;
import com.example.ProJectLP.domain.song.SongRepository;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.SongRequestDto;
import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;
    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;

    //트랙리스트 수정
    @Transactional
    public ResponseEntity<?> updateSong(Long vinylid, SongRequestDto songs, HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            throw new PrivateException(ErrorCode.LOGIN_REQUIRED);
        }

        Member member = validateMember(request);
        if (null == member) {
            throw new PrivateException(ErrorCode.LOGIN_NOTFOUND_MEMBER);
        }

        Vinyl vinyl = isPresentVinyl(vinylid);
        if (null == vinyl) {
            throw new PrivateException(ErrorCode.VINYL_NOTFOUND);
        }

        if (!member.isRole()) {
            throw new PrivateException(ErrorCode.VINYL_SONG_MODIFY_FORBIDDEN);
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
        return ResponseEntity.ok(Map.of("msg", "트랙리스트 수정이 완료 됐습니다.","data", vinyl.getId()));
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
