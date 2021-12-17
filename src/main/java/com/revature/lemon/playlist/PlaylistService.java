package com.revature.lemon.playlist;

import com.revature.lemon.common.exceptions.ResourceNotFoundException;
import com.revature.lemon.common.model.SongPlaylist;
import com.revature.lemon.common.model.SongPlaylistKey;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.AddUserRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.playlist.dtos.responses.PlaylistResponse;
import com.revature.lemon.song.Song;
import com.revature.lemon.user.User;
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

    /**
     * @return all playlists listed as "PUBLIC"
     */
    public List<PlaylistResponse> getPublicPlaylists() {

        List<Playlist> publicList = playlistRepository.findPlaylistByAccess(AccessType.PUBLIC);

        return publicList.stream()
                         .map(PlaylistResponse::new)
                         .collect(Collectors.toList());
    }

    /**
     * @param userId id of the current user
     * @return all playlists listed as "PRIVATE" that the current user has access to
     */
    public List<PlaylistResponse> getPrivatePlaylists(String userId) {

        //todo do we want this toString here or no? If no... find way to get query to take AccessType
        List<Playlist> privateList = playlistRepository.findPlaylistByAccessAndId(AccessType.PRIVATE.toString(), userId);
        return privateList.stream()
                          .map(PlaylistResponse::new)
                          .collect(Collectors.toList());
    }

    /**
     * Delete playlist of the given playlistId
     * @param playlistId id of the playlist to be deleted
     */
    public void deletePlaylist(String playlistId) {
        playlistRepository.deleteById(playlistId);
    }

    // Currently, this works... it just needs a way to get the particular user's ID which is in the users table; We are in the playlistService class
    public void addUserToPlaylist(String playlistId, AddUserRequest newUser) {
        Playlist playlist = playlistRepository.findById(playlistId)
                                              .orElseThrow(ResourceNotFoundException::new);

        /**
         * Request a new user to be added
         * We have the username and discriminator ONLY
         * so we have to get the ID from the userRepository
         */
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setDiscriminator(newUser.getDiscriminator());
        user.setId("1234");

        playlist.addUser(user, newUser.getUserRole());
        playlistRepository.save(playlist);
    }

    //todo removeUserFromPlaylist, given username and discriminator unless maybe the frontend has access to the user ID already? and they can send that back

    //todo findUserWithPlaylistAccess, return a list of all users that has userRoles to the particular playlist

}
