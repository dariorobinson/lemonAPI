package com.revature.lemon.playlist;

import com.revature.lemon.common.exceptions.ResourceNotFoundException;
import com.revature.lemon.common.model.SongPlaylistOrder;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.song.Song;
import com.revature.lemon.song.dtos.NewSongRequest;
import com.revature.lemon.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
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

    public void addSongToPlaylist(AddSongRequest newSongRequest) {

        Song song = new Song();
        System.out.println(newSongRequest.getPlaylistId());
        Playlist playlist = playlistRepository.findById(newSongRequest.getPlaylistId()).orElseThrow(ResourceNotFoundException::new);
        System.out.println(newSongRequest.getSongList().get(0));
        System.out.println(newSongRequest.getSongList().get(1));
        System.out.println(newSongRequest.getSongList().get(2));
        if (playlist.getSongOrder() == null) {
            System.out.println("is null");
            List<SongPlaylistOrder> newSongOrder = new ArrayList<>();
            playlist.setSongOrder(newSongOrder);
            playlist.addSong(song);
        } else {
            List<SongPlaylistOrder> newSongListOrder;
            List<Song> songList = newSongRequest.getSongList();

            newSongListOrder = songList.stream().map(SongPlaylistOrder::new).collect(Collectors.toList());
            playlist.setSongOrder(newSongListOrder);
        }


        System.out.println(playlist.getSongOrder().get(0));
        playlistRepository.save(playlist);
    }




}
