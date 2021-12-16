package com.revature.lemon.playlist;

import com.revature.lemon.common.exceptions.ResourceNotFoundException;
import com.revature.lemon.common.model.SongPlaylist;
import com.revature.lemon.common.model.SongPlaylistKey;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.song.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Playlist playlist = new Playlist(playlistRequest);
        playlist.setId(UUID.randomUUID().toString());
        playlistRepository.save(playlist);
        return playlist;
    }

    /**
     * Grabs the list of songs from newSongRequest and maps it to an associate table between songs and playlist.
     * Set that table to playlist then save
     * @param newSongRequest contains a playlistId and a list of songs
     */
    public void addSongsToPlaylist(AddSongRequest newSongRequest) {

        //load up the playlist
        Playlist playlist = playlistRepository.findById(newSongRequest.getPlaylistId())
                                              .orElseThrow(ResourceNotFoundException::new);

        List<Song> songList = newSongRequest.getSongList();
        List<SongPlaylist> newSongOrderList = songList.stream()
                                                 .map(SongPlaylist::new)
                                                 .collect(Collectors.toList());
        //For each song in the request, make a new composite key and assign its order from its position in the list
        for(int i=0; i < songList.size(); i++) {
            Song song = songList.get(i);
            newSongOrderList.get(i).setId(new SongPlaylistKey(song.getUrl(),playlist.getId()));
            newSongOrderList.get(i).setSongOrder(i+1);
        }
        playlist.setSongOrderList(newSongOrderList);
        playlistRepository.save(playlist);
    }

}
