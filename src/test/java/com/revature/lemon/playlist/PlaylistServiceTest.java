package com.revature.lemon.playlist;

import com.revature.lemon.common.exceptions.PlaylistNotFoundException;
import com.revature.lemon.common.exceptions.ResourceNotFoundException;
import com.revature.lemon.common.model.SongPlaylist;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.playlist.dtos.requests.AddSongRequest;
import com.revature.lemon.playlist.dtos.requests.AddUserRequest;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.playlist.dtos.requests.RemoveUserRequest;
import com.revature.lemon.playlist.dtos.responses.PlaylistResponse;
import com.revature.lemon.song.Song;
import com.revature.lemon.song.SongRepository;
import com.revature.lemon.user.User;
import com.revature.lemon.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {


    PlaylistService sut;
    PlaylistRepository mockPlaylistRepository;
    UserRepository mockUserRepository;
    SongRepository mockSongRepository;

    @BeforeEach
    public void testSetup() {
        mockPlaylistRepository = mock(PlaylistRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockSongRepository = mock(SongRepository.class);
        sut = new PlaylistService(mockPlaylistRepository, mockUserRepository, mockSongRepository);
    }

    @AfterEach
    public void cleanTestSetup() {
        sut = null;
    }

    @Test
    public void test_createNewPlaylist_returnsPlaylistResponse_givenNewPlayerRequest() {

        NewPlaylistRequest newPlaylistRequest = new NewPlaylistRequest();
        newPlaylistRequest.setCreator(new User());
        newPlaylistRequest.setAccess(AccessType.PRIVATE);
        newPlaylistRequest.setDescription("description");
        newPlaylistRequest.setName("name");

        Playlist playlist = new Playlist(newPlaylistRequest);
        PlaylistResponse expectedResult = new PlaylistResponse(playlist);
        when(mockPlaylistRepository.save(any())).thenReturn(playlist);

        PlaylistResponse actualResult = sut.createNewPlaylist(newPlaylistRequest);
        expectedResult.setId(actualResult.getId());

        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void test_addSongInPlaylist_runsOnce_givenValidAddSongRequest() {

        AddSongRequest addSongRequest = new AddSongRequest();
        addSongRequest.setPlaylistId(UUID.randomUUID().toString());
        Song song = new Song();
        song.setUrl(addSongRequest.getSongUrl());
        song.setDuration(addSongRequest.getDuration());
        song.setName(addSongRequest.getName());

        Playlist playlist = new Playlist();
        List<SongPlaylist> songList = new LinkedList<>();
        playlist.setSongOrderList(songList);
        playlist.setId(addSongRequest.getPlaylistId());
        SongPlaylist songPlaylist = new SongPlaylist();
        songPlaylist.setSong(song);
        playlist.addSong(songPlaylist);

        when(mockPlaylistRepository.findById(addSongRequest.getPlaylistId())).thenReturn(java.util.Optional.of(playlist));
        when(mockPlaylistRepository.save(playlist)).thenReturn(playlist);
        when(mockSongRepository.findById(song.getUrl())).thenReturn(java.util.Optional.of(song));

        sut.addSongInPlaylist(addSongRequest);

        verify(mockPlaylistRepository, times(1)).findById(addSongRequest.getPlaylistId());
        verify(mockSongRepository, times(1)).findById(song.getUrl());
        verify(mockPlaylistRepository, times(1)).save(playlist);
    }

    @Test
    public void test_addSongInPlaylist_getsSongFromRepository_givenSongUrlInDatabase() {

        AddSongRequest addSongRequest = new AddSongRequest();
        addSongRequest.setPlaylistId(UUID.randomUUID().toString());
        Song song = new Song();
        song.setUrl(addSongRequest.getSongUrl());
        song.setDuration(addSongRequest.getDuration());
        song.setName(addSongRequest.getName());

        Playlist playlist = new Playlist();
        List<SongPlaylist> songList = new LinkedList<>();
        playlist.setSongOrderList(songList);
        playlist.setId(addSongRequest.getPlaylistId());
        SongPlaylist songPlaylist = new SongPlaylist();
        songPlaylist.setSong(song);
        playlist.addSong(songPlaylist);

        when(mockPlaylistRepository.findById(addSongRequest.getPlaylistId())).thenReturn(java.util.Optional.of(playlist));
        when(mockSongRepository.findById(song.getUrl())).thenReturn(java.util.Optional.of(song));

        sut.addSongInPlaylist(addSongRequest);

        verify(mockPlaylistRepository, times(1)).findById(addSongRequest.getPlaylistId());
        verify(mockSongRepository, times(1)).findById(song.getUrl());
        verify(mockPlaylistRepository, times(0)).save(playlist);

    }

    @Test
    public void test_addSongInPlaylist_throwsPlaylistNotFoundException_givenUnusedPlaylistId() {

        AddSongRequest addSongRequest = new AddSongRequest();

        PlaylistNotFoundException thrown = Assertions.assertThrows(PlaylistNotFoundException.class, () -> sut.addSongInPlaylist(addSongRequest), "Expected invalid/unused playlist Id but got valid/used playlistid");
        Assertions.assertTrue(thrown.getMessage().contains("No playlist was found using using that id"));
    }

    @Test
    public void test_getPublicPlaylists_returnsListOfPlaylistResponse() {

        List<PlaylistResponse> expectedList = new LinkedList<>();

        when(mockPlaylistRepository.findPlaylistByAccess(AccessType.PUBLIC).stream().map(PlaylistResponse::new).collect(Collectors.toList())).thenReturn(expectedList);
        List<PlaylistResponse> actualList = sut.getPublicPlaylists();

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void test_getPrivatePlaylists_returnsListOfPlaylistResponse_giveUserId() {

        String userId = "";
        List<PlaylistResponse> expectedList = new LinkedList<>();

        when(mockPlaylistRepository.findPlaylistByAccessAndId(AccessType.PRIVATE, userId).stream().map(PlaylistResponse::new).collect(Collectors.toList())).thenReturn(expectedList);
        List<PlaylistResponse> actualList = sut.getPrivatePlaylists(userId);

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void test_deletePlaylist_runsOnce_givenPlaylistId() {
        String playlistId = "";

        sut.deletePlaylist(playlistId);

        verify(mockPlaylistRepository,times(1)).deleteById(playlistId);
    }

    @Test
    public void test_addUserToPlaylist_returnsNothing_givenPlaylistIdAndAddUserRequest() {

        String playlistId = "";
        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("valid");
        addUserRequest.setDiscriminator("1234");
        addUserRequest.setUserRole(RoleType.CREATOR);
        Playlist playlist = new Playlist();
        playlist.setUserRoleList(new LinkedList<>());
        User user = new User();
        user.setUsername(addUserRequest.getUsername());
        user.setDiscriminator(addUserRequest.getDiscriminator());
        playlist.addUser(user, addUserRequest.getUserRole());

        when(mockPlaylistRepository.findById(playlistId)).thenReturn(java.util.Optional.of(playlist));
        when(mockUserRepository.findUserByUsernameAndDiscriminator(addUserRequest.getUsername(), addUserRequest.getDiscriminator())).thenReturn(java.util.Optional.of(user));
        when(mockPlaylistRepository.save(playlist)).thenReturn(playlist);

        sut.addUserToPlaylist(playlistId, addUserRequest);

        verify(mockPlaylistRepository, times(1)).findById(playlistId);
        verify(mockUserRepository, times(1)).findUserByUsernameAndDiscriminator(addUserRequest.getUsername(), addUserRequest.getDiscriminator());
        verify(mockPlaylistRepository, times(1)).save(playlist);
    }

    @Test
    public void test_editUserRoleInPlaylist_runsSuccessfully_givenPlaylistIdAndAddUserRequest() {

        String playlistId = "";
        AddUserRequest editUserRequest = new AddUserRequest();
        editUserRequest.setUserRole(RoleType.EDITOR);
        editUserRequest.setUsername("valid");
        editUserRequest.setDiscriminator("1234");
        Playlist playlist = new Playlist();
        User user = new User();
        user.setUsername(editUserRequest.getUsername());
        user.setDiscriminator(editUserRequest.getDiscriminator());
        playlist.setUserRoleList(new LinkedList<>());
        playlist.addUser(user, RoleType.EDITOR);

        when(mockPlaylistRepository.findById(playlistId)).thenReturn(java.util.Optional.of(playlist));
        when(mockPlaylistRepository.save(playlist)).thenReturn(playlist);

        sut.editUserRoleInPlaylist(playlistId, editUserRequest);

        verify(mockPlaylistRepository, times(1)).findById(playlistId);
        verify(mockPlaylistRepository, times(1)).save(playlist);
    }

    @Test
    public void test_editUserRoleInPlaylist_throwsResourceNotFoundException_givenPlaylistIdAddUserRequestAndTheUserHasNoRoleInPlaylist() {

        String playlistId = "";
        RemoveUserRequest removeUserRequest = new RemoveUserRequest();
        removeUserRequest.setUsername("valid");
        removeUserRequest.setDiscriminator("1234");
        Playlist playlist = new Playlist();
        playlist.setUserRoleList(new LinkedList<>());

        when(mockPlaylistRepository.findById(playlistId)).thenReturn(java.util.Optional.of(playlist));
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> sut.removeUserFromPlaylist(playlistId, removeUserRequest), "Expected for user to have no role in playlist but they did");

        Assertions.assertTrue(thrown.getMessage().contains("User did not have any roles in this playlist"));
    }

    @Test
    public void test_removeUserFromPlaylist_runsSuccessfully_givenPlaylistIdRemoveUserRequestAndUserHasARoleInPlaylist() {

        String playlistId = "";
        RemoveUserRequest removeUserRequest = new RemoveUserRequest();
        removeUserRequest.setUsername("valid");
        removeUserRequest.setDiscriminator("1234");
        Playlist playlist = new Playlist();
        User user = new User();
        user.setUsername(removeUserRequest.getUsername());
        user.setDiscriminator(removeUserRequest.getDiscriminator());
        playlist.setUserRoleList(new LinkedList<>());
        playlist.addUser(user, RoleType.EDITOR);

        when(mockPlaylistRepository.findById(playlistId)).thenReturn(java.util.Optional.of(playlist));
        when(mockPlaylistRepository.save(playlist)).thenReturn(playlist);

        sut.removeUserFromPlaylist(playlistId, removeUserRequest);

        verify(mockPlaylistRepository, times(1)).findById(playlistId);
        verify(mockPlaylistRepository, times(1)).save(playlist);
    }

    @Test
    public void test_removeUserFromPlaylist_throwsResourceNotFoundException_givenPlaylistIdRemoveUserRequestAndTheUserHasNoRoleInPlaylist() {

        String playlistId = "";
        RemoveUserRequest removeUserRequest = new RemoveUserRequest();
        removeUserRequest.setUsername("valid");
        removeUserRequest.setDiscriminator("1234");
        Playlist playlist = new Playlist();
        playlist.setUserRoleList(new LinkedList<>());

        when(mockPlaylistRepository.findById(playlistId)).thenReturn(java.util.Optional.of(playlist));
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> sut.removeUserFromPlaylist(playlistId, removeUserRequest), "Expected for user to have no role in playlist but they did");

        Assertions.assertTrue(thrown.getMessage().contains("User did not have any roles in this playlist"));
    }


}
