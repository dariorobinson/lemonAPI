package com.revature.lemon.playlist;

import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.common.util.web.Authenticated;
import com.revature.lemon.common.util.web.Secured;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.AddUserRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.playlist.dtos.responses.PlaylistResponse;
import com.revature.lemon.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @PatchMapping(value = "/{playlistId}/addsong", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Authenticated //Change to new annotation. Having to do with the user role.
    //todo perhaps change this name to editSongsInPlaylist? since it technically can remove songs as well since it takes in a list of songs
    public void addSongsToPlaylist(@PathVariable String playlistId, @RequestBody AddSongRequest newSongRequest) {
        newSongRequest.setPlaylistId(playlistId);
        playlistService.addSongsToPlaylist(newSongRequest);
    }

    //consider making a UserPlaylistRoleService or a UserPlaylistRoleRepository that PlaylistService gets injected with
    //put in a username and discriminator, UserPlaylistRole should be getting updated
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{playlistId}/adduser", consumes = "application/json", produces = "application/json")
    public Playlist addUserToPlaylist(@PathVariable String playlistId, @RequestBody AddUserRequest user) {
        playlistService.addUserToPlaylist(playlistId, user);
        return null;
    }

    //todo removeUserFromPlaylist

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{playlistId}")
    @Secured(allowedAccountTypes = {RoleType.CREATOR}, playlistId = "args(playlistId)")
    public void deletePlaylist(@PathVariable String playlistId) {

        System.out.println("Deleting playlist!");
        playlistService.deletePlaylist(playlistId);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/private", produces = "application/json")
    public List<PlaylistResponse> viewPrivatePlaylists(HttpSession session) {

        User user = (User) session.getAttribute("authUser");
        return playlistService.getPrivatePlaylists(user.getId());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/public", produces = "application/json")
    public List<PlaylistResponse> viewPublicPlaylists() {

        return playlistService.getPublicPlaylists();
    }
}
