package com.revature.lemon.playlist;

import com.revature.lemon.song.SongRepository;
import com.revature.lemon.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

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

    //public void test_

}
