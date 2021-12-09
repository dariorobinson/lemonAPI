package com.revature.lemon.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService (PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }
}
