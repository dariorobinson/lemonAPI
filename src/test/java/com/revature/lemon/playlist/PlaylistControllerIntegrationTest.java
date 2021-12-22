package com.revature.lemon.playlist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.playlist.dtos.requests.*;
import com.revature.lemon.user.User;
import io.jsonwebtoken.lang.Assert;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PlaylistControllerIntegrationTest {

    private MockMvc mockMvc;
    private WebApplicationContext context;
    private ObjectMapper mapper;

    String playlistId = "5b5d925d-e30e-48aa-a72c-61eeca661b6f";
    String authorizedUser = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJzdWIiOiJDcmVhdG9yVXNlciIsImlzcyI6ImxlbW9uIiwiZGlzY3JpbWluYXRvciI6IjMyMTQiLCJpYXQiOjE2NDAxOTU3MDYsImV4cCI6MTY0MDI4MjEwNn0.rgYXIcZ6-vzIIZIo4Irkx0pLF_cxcodbAs4Wh5cZis0";
    String unauthorizedUser = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NSIsInN1YiI6IlZpZXdlclVzZXIiLCJpc3MiOiJsZW1vbiIsImRpc2NyaW1pbmF0b3IiOiIzMjE0MTIiLCJpYXQiOjE2NDAyMTI5MDksImV4cCI6MTY0MDI5OTMwOX0.Se4D0yGEYBQx7tDI1C2pCfdxzTPW6kWWqZHOvw7tAHU";
    String songUrl = "youtube.com";

    @Autowired
    public PlaylistControllerIntegrationTest(WebApplicationContext context, ObjectMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    @BeforeEach
    public void setupTest() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test_createPlaylist_returns201_givenNewPlaylistRequest() throws Exception {

        User user = new User();
        user.setUsername("username");
        user.setDiscriminator("1234");
        user.setId("id");

        NewPlaylistRequest newPlaylistRequest = new NewPlaylistRequest();
        newPlaylistRequest.setName("playlist");
        newPlaylistRequest.setDescription("description");
        newPlaylistRequest.setAccess(AccessType.PRIVATE);
        newPlaylistRequest.setCreator(user);

        String requestPayload = mapper.writeValueAsString(newPlaylistRequest);


        MvcResult result = mockMvc.perform(post("/playlists").contentType("application/json").content(requestPayload)
                                  .header("Authorization", authorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(201))
                                  .andExpect(jsonPath("$.id").isNotEmpty())
                                  .andReturn();

        assertEquals("application/json", result.getResponse().getContentType());
    }

    @Test
    public void test_editPlaylist_returns200_givenEditPlaylistRequest() throws Exception {

        EditPlaylistRequest editPlaylistRequest = new EditPlaylistRequest();
        editPlaylistRequest.setPlaylistId(playlistId);
        editPlaylistRequest.setName("name");
        editPlaylistRequest.setDescription("description");
        editPlaylistRequest.setAccess(AccessType.PUBLIC);

        String requestPayload = mapper.writeValueAsString(editPlaylistRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/editplaylist").contentType("application/json").content(requestPayload)
                .header("Authorization", authorizedUser))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        assertEquals("application/json", result.getResponse().getContentType());

    }

    @Test
    public void test_editPlaylist_returns403_givenUnauthorizedUser() throws Exception {

        EditPlaylistRequest editPlaylistRequest = new EditPlaylistRequest();
        editPlaylistRequest.setPlaylistId(playlistId);
        editPlaylistRequest.setName("name");
        editPlaylistRequest.setDescription("description");
        editPlaylistRequest.setAccess(AccessType.PUBLIC);

        String requestPayload = mapper.writeValueAsString(editPlaylistRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/editplaylist").contentType("application/json").content(requestPayload)
                                  .header("Authorization", unauthorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(403))
                                  .andReturn();

        assertEquals("application/json", result.getResponse().getContentType());

    }

    @Test
    public void test_addSongInPlaylist_returns204_givenAddSongRequestAndAuthorizedUser() throws Exception {


        AddSongRequest addSongRequest = new AddSongRequest();
        addSongRequest.setPlaylistId(playlistId);
        addSongRequest.setName("name");
        addSongRequest.setSongUrl("asadf");

        String resultPayload = mapper.writeValueAsString(addSongRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/addsong").contentType("application/json").content(resultPayload)
                                  .header("Authorization", authorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(204))
                                  .andReturn();
    }

    @Test
    public void test_addSongInPlaylist_returns403_givenAddSongRequestAndUnauthorizedUser() throws Exception {


        AddSongRequest addSongRequest = new AddSongRequest();
        addSongRequest.setPlaylistId(playlistId);
        addSongRequest.setName("name");

        String resultPayload = mapper.writeValueAsString(addSongRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/addsong").contentType("application/json").content(resultPayload)
                                  .header("Authorization", unauthorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(403))
                                  .andReturn();
    }

    @Test
    public void test_deleteSongFromPlaylist_returns204_givenRemoveSongRequestAndAuthorizedUser() throws Exception {

        RemoveSongRequest removeSongRequest = new RemoveSongRequest();
        removeSongRequest.setPlaylistId(playlistId);
        removeSongRequest.setSongUrl(songUrl);

        String resultPayload = mapper.writeValueAsString(removeSongRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/removesong").contentType("application/json").content(resultPayload)
                                  .header("Authorization", authorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(204))
                                  .andReturn();
    }

    @Test
    public void test_deleteSongFromPlaylist_returns204_givenRemoveSongRequestAndUnauthorizedUser() throws Exception {

        RemoveSongRequest removeSongRequest = new RemoveSongRequest();
        removeSongRequest.setPlaylistId(playlistId);
        removeSongRequest.setSongUrl(songUrl);

        String resultPayload = mapper.writeValueAsString(removeSongRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/removesong").contentType("application/json").content(resultPayload)
                                  .header("Authorization", unauthorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(403))
                                  .andReturn();
    }

    @Test
    public void test_addUserToPlaylist_returns204_givenAddUserRequestAndAuthorizedUser() throws Exception {

        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("EditorUser");
        addUserRequest.setDiscriminator("32141");
        addUserRequest.setUserRole(RoleType.EDITOR);

        String payload = mapper.writeValueAsString(addUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/adduser").contentType("application/json").content(payload)
                                  .header("Authorization", authorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(204))
                                  .andReturn();
    }

    @Test
    public void test_addUserToPlaylist_returns204_givenAddUserRequestAndUnauthorizedUser() throws Exception {

        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("EditorUser");
        addUserRequest.setDiscriminator("32141");
        addUserRequest.setUserRole(RoleType.EDITOR);

        String payload = mapper.writeValueAsString(addUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/adduser").contentType("application/json").content(payload)
                                  .header("Authorization", unauthorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(403))
                                  .andReturn();
    }

    @Test
    public void test_removeUserFromPlaylist_returns204_givenRemoveUserRequestAndAuthorizedUser() throws Exception {

        RemoveUserRequest removeUserRequest = new RemoveUserRequest();
        removeUserRequest.setUsername("EditorUser");
        removeUserRequest.setDiscriminator("32141");

        String payload = mapper.writeValueAsString(removeUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/removeuser").contentType("application/json").content(payload)
                                  .header("Authorization", authorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(204))
                                  .andReturn();
    }

    @Test
    public void test_removeUserFromPlaylist_returns403_givenRemoveUserRequestAndUnauthorizedUser() throws Exception {

        RemoveUserRequest removeUserRequest = new RemoveUserRequest();
        removeUserRequest.setUsername("EditorUser");
        removeUserRequest.setDiscriminator("32141");

        String payload = mapper.writeValueAsString(removeUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/removeuser").contentType("application/json").content(payload)
                                  .header("Authorization", unauthorizedUser))
                                  .andDo(print())
                                  .andExpect(status().is(403))
                                  .andReturn();
    }

    @Test
    public void test_editUserRoleInPlaylist_returns204_givenAddUserRequestAndAuthorizedUser() throws Exception {

        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("EditorUser");
        addUserRequest.setDiscriminator("32141");
        addUserRequest.setUserRole(RoleType.VIEWER);

        String payload = mapper.writeValueAsString(addUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/edituser").contentType("application/json").content(payload)
                .header("Authorization", authorizedUser))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void test_editUserRoleInPlaylist_returns204_givenAddUserRequestAndUnauthorizedUser() throws Exception {

        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("EditorUser");
        addUserRequest.setDiscriminator("32141");
        addUserRequest.setUserRole(RoleType.VIEWER);

        String payload = mapper.writeValueAsString(addUserRequest);

        MvcResult result = mockMvc.perform(patch("/playlists/" + playlistId + "/edituser").contentType("application/json").content(payload)
                        .header("Authorization", unauthorizedUser))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void test_deletePlaylist_returns204_givenPlaylistIdAndAuthorizedUser() {


        MvcResult result = mockMvc.perform(delete("/playlists/" + playlistId)
                .header("Authorization"))
    }




}
