package com.example.ProJectLP.dto.request;

import com.example.ProJectLP.domain.song.Song;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SongRequestDto {

    private List<Song> songs;
}
