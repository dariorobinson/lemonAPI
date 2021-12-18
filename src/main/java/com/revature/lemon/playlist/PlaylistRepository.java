package com.revature.lemon.playlist;

import com.revature.lemon.common.util.AccessType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, String> {

    List<Playlist> findPlaylistByAccess(AccessType access);

    @Query("FROM Playlist p JOIN UserPlaylist up ON p.id = up.id.playlistId WHERE up.id.userId = :userId AND p.access = :access")
    List<Playlist> findPlaylistByAccessAndId(AccessType access, String userId);

}
