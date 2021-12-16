package com.revature.lemon.song;

import com.revature.lemon.song.dtos.NewSongRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping(consumes = "application/json")
    public Song addSongToDatabase(@RequestBody NewSongRequest newSong) {
        return songService.addSong(newSong);
    }


}
