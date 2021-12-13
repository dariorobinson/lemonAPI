package com.revature.lemon.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService (PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createNewPlaylist(@Valid Playlist playlist ) {

        // Playlist playlist = new Playlist();
        playlistRepository.save(playlist);
        return playlist;
    }
}
