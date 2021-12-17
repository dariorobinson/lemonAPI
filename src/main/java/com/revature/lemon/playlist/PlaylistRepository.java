package com.revature.lemon.playlist;

import com.revature.lemon.common.util.AccessType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, String> {

    List<Playlist> findPlaylistByAccess(AccessType access);

    //@Query("FROM UserPlaylist up JOIN Playlist p ON p.id = up.id.playlistId WHERE up.id.userId = :userId AND p.access = :access")
    // Works as well but has some hiccups that need to be fixed also needs to return UserPlaylist list
    @Query(value = "select * FROM user_playlist up INNER JOIN playlists p ON p.playlist_id = up.playlist_id WHERE up.user_id = :userId AND p.access = :access", nativeQuery = true)
    List<Playlist> findPlaylistByAccessAndId(String access, String userId);

}
