package com.revature.lemon.playlist;

import com.revature.lemon.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class PlaylistServiceTest {


    PlaylistService sut;
    PlaylistRepository mockPlaylistRepository;
    UserRepository mockUserRepository;

    @BeforeEach
    public void testSetup() {
        mockPlaylistRepository = mock(PlaylistRepository.class);
        mockUserRepository = mock(UserRepository.class);
        sut = new PlaylistService(mockPlaylistRepository, mockUserRepository);
    }

    @AfterEach
    public void cleanTestSetup() {
        sut = null;
    }

    public void test_

}
