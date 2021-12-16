package com.revature.lemon.playlist;

import com.revature.lemon.common.util.web.Authenticated;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.AddUserRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.song.dtos.NewSongRequest;
import com.revature.lemon.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Authenticated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Playlist createPlaylist(@RequestBody NewPlaylistRequest playlist, HttpSession session) {
        playlist.setCreator((User) session.getAttribute("authUser"));
        return playlistService.createNewPlaylist(playlist);
    }

    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Authenticated //Change to new annotation. Having to do with the user role.
    public void addSongToPlaylist(@RequestBody AddSongRequest newSongRequest) {

        playlistService.addSongToPlaylist(newSongRequest);
    }

    //consider making a UserPlaylistRoleService or a UserPlaylistRoleRepository that PlaylistService gets injected with
    //put in a username and discriminator, UserPlaylistRole should be getting updated
    //since we can also edit song order in playlist as well as user roles... need to consider mapping
    @PatchMapping(consumes = "application/json", produces = "application/json")
    public Playlist addUserToPlaylist(@PathVariable String id, @RequestBody AddUserRequest user) {
        return null;
    }

    @DeleteMapping
    public void deletePlaylist() {

    }
}
