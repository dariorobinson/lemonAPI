package com.revature.lemon.playlist;

import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService (PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    /**
     * Turns the playlistRequest into a playlist to be persisted to the database
     */
    public Playlist createNewPlaylist(@Valid NewPlaylistRequest playlistRequest) {
        User user = playlistRequest.getCreator();
        Playlist playlist = new Playlist();
        playlist.setId(UUID.randomUUID().toString());
        playlist.setName(playlistRequest.getName());
        playlist.setDescription(playlistRequest.getDescription());
        playlist.setAccess(playlistRequest.getAccess());
        playlist.addCreator(user);
        playlistRepository.save(playlist);
        return playlist;
    }
}
