package com.revature.lemon.models;

import javax.persistence.*;
import javax.xml.datatype.Duration;

@Entity
@Table(name = "songs")
public class song {

    @Id
    @Column(name = "song_url", nullable = false, unique = true)
    private String song_url;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR CHECK (LENGTH(name) >= 1)")
    private String songName;

    @Column(name = "duration", nullable = false)
    private Duration duration;

    public song(String song_url, String name, Duration duration) {
        this.song_url = song_url;
        this.songName = name;
        this.duration = duration;
    }

    // no arg constructor (just in case we need it)
    public song() {
        super();
    }

    public String getSong_url() {
        return song_url;
    }

    public void setSong_url(String song_url) {
        this.song_url = song_url;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "songs{" +
                "song_url='" + song_url + '\'' +
                ", name='" + songName + '\'' +
                ", duration=" + duration +
                '}';
    }
}
