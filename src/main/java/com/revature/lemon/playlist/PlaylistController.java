package com.revature.lemon.playlist;

import com.revature.lemon.auth.TokenService;
import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.common.util.web.Authenticated;
import com.revature.lemon.common.util.web.Secured;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.AddUserRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.playlist.dtos.requests.RemoveUserRequest;
import com.revature.lemon.playlist.dtos.responses.PlaylistResponse;
import com.revature.lemon.playlist.dtos.responses.SongsInPlaylistResponse;
import com.revature.lemon.playlist.dtos.responses.UsersInPlaylistResponse;
import com.revature.lemon.user.User;
import com.revature.lemon.user.dtos.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final TokenService tokenService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, TokenService tokenService) {
        this.playlistService = playlistService;
        this.tokenService = tokenService;
    }

    @Authenticated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public PlaylistResponse createPlaylist(@RequestBody NewPlaylistRequest playlist, @RequestHeader("Authorization") String token) {
        LoginRequest user = tokenService.extractTokenDetails(token);
        User creator = new User(user.getId(), user.getUsername(), user.getDiscriminator());
        playlist.setCreator(creator);
        return playlistService.createNewPlaylist(playlist);
    }

    @PatchMapping(value = "/{playlistId}/addsong", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(allowedAccountTypes = {RoleType.CREATOR, RoleType.EDITOR}) //Change to new annotation. Having to do with the user role.
    public void editSongsInPlaylist(@PathVariable String playlistId, @RequestBody AddSongRequest newSongRequest, @RequestHeader("Authorization") String token) {

        newSongRequest.setPlaylistId(playlistId);
        playlistService.editSongsInPlaylist(newSongRequest);
    }

    //consider making a UserPlaylistRoleService or a UserPlaylistRoleRepository that PlaylistService gets injected with
    //put in a username and discriminator, UserPlaylistRole should be getting updated
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{playlistId}/adduser", consumes = "application/json", produces = "application/json")
   // @Secured(allowedAccountTypes = {RoleType.CREATOR}) // get user id from username + discriminator in service class
    public PlaylistResponse addUserToPlaylist(@PathVariable String playlistId, @RequestBody AddUserRequest newUser) {

        playlistService.addUserToPlaylist(playlistId, newUser);
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{playlistId}/removeuser", consumes = "application/json")
    //@Secured(allowedAccountTypes = {RoleType.CREATOR})
    public void removeUserFromPlaylist(@PathVariable String playlistId, @RequestBody RemoveUserRequest userRequest) {
        playlistService.removeUserFromPlaylist(playlistId, userRequest);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{playlistId}/edituser")
    //@Secured(allowedAccountTypes = {RoleType.CREATOR})
    public void editUserRole(@PathVariable String playlistId, @RequestBody AddUserRequest updateUser) {

        playlistService.editUserRoleInPlaylist(playlistId, updateUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{playlistId}")
    //@Secured(allowedAccountTypes = {RoleType.CREATOR})
    public void deletePlaylist(@PathVariable String playlistId) {

        playlistService.deletePlaylist(playlistId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{playlistId}/getusers")
    //@Authenticated
    public List<UsersInPlaylistResponse> getUsersWithRoles(@PathVariable String playlistId) {

        return playlistService.getUsersWithPlaylistAccess(playlistId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{playlistId}/getsongs", produces = "application/json")
    //@Authenticated
    public List<SongsInPlaylistResponse> getSongsInPlaylist(@PathVariable String playlistId) {

        return playlistService.getSongsInPlaylist(playlistId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/private", produces = "application/json")
   // @Authenticated
    public List<PlaylistResponse> getPrivatePlaylists(HttpSession session) {

        User user = (User) session.getAttribute("authUser");
        return playlistService.getPrivatePlaylists(user.getId());
    }

    //@Authenticated
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/public", produces = "application/json")
    public List<PlaylistResponse> getPublicPlaylists() {
//        User user = (User) session.getAttribute("authUser");
//        System.out.println("Authuser is: " + session.getAttribute("authUser"));
//        System.out.println(user);

        return playlistService.getPublicPlaylists();
    }
}
