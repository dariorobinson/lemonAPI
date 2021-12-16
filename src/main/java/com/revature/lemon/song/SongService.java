package com.revature.lemon.song;

import com.revature.lemon.song.dtos.NewSongRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song addSong(NewSongRequest newSong) {

        Song song = songRepository.findById(newSong.getUrl()).orElse(null);
        if (song == null) {
            song = new Song();
            song.setUrl(newSong.getUrl());
            songRepository.save(song);
        }

        return song;
    }


}
