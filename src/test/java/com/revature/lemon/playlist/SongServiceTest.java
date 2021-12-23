package com.revature.lemon.playlist;

import com.revature.lemon.song.Song;
import com.revature.lemon.song.SongRepository;
import com.revature.lemon.song.SongService;
import com.revature.lemon.song.dtos.NewSongRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class SongServiceTest {

    SongService sut;
    SongRepository mockSongRepository;

    @BeforeEach
    public void test_setup() {
        mockSongRepository = mock(SongRepository.class);
        sut = new SongService(mockSongRepository);
    }

    @Test
    public void test_addSong_returnsSong_givenNewSongRequestInDatabase() {

        NewSongRequest newSongRequest = new NewSongRequest();
        newSongRequest.setName("Song name");
        newSongRequest.setUrl("song link");

        Song expectedResult = new Song();
        expectedResult.setName(newSongRequest.getName());
        expectedResult.setUrl(newSongRequest.getUrl());

        when(mockSongRepository.findById(anyString())).thenReturn(java.util.Optional.of(expectedResult));

        Song actualResult = sut.addSong(newSongRequest);

        Assertions.assertEquals(expectedResult, actualResult, "Expected these to have same name and Url but they didn't");
        verify(mockSongRepository, times(1)).findById(newSongRequest.getUrl());
        verify(mockSongRepository, times(0)).save(expectedResult);
    }

/*
    @Test
    public void test_addsong_returnsSong_givenNewSongRequestNotInDatabase() {

        NewSongRequest newSongRequest = new NewSongRequest();
        newSongRequest.setName("Song name");
        newSongRequest.setUrl("song link");

        Song expectedResult = new Song();
        expectedResult.setName(newSongRequest.getName());
        expectedResult.setUrl(newSongRequest.getUrl());

        when(mockSongRepository.findById(newSongRequest.getUrl())).thenReturn(null);

        Song actualResult = sut.addSong(newSongRequest);

        verify(mockSongRepository, times(1)).findById(newSongRequest.getUrl());
        verify(mockSongRepository, times(1)).save(expectedResult);
    }

    */
}
