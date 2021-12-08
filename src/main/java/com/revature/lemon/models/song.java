package com.revature.lemon.models;

import javax.xml.datatype.Duration;

public class song {

    private String song_url;
    private String name;
    private Duration duration;

    public song(String song_url, String name, Duration duration) {
        this.song_url = song_url;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
