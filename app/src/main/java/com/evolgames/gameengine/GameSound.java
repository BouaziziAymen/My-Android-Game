package com.evolgames.gameengine;

import org.andengine.audio.sound.Sound;

import java.util.List;

public class GameSound {
    private List<Sound> soundList;
    private String title;

    public GameSound(List<Sound> soundList, String title) {
        this.soundList = soundList;
        this.title = title;
    }

    public List<Sound> getSoundList() {
        return soundList;
    }

    public String getTitle() {
        return title;
    }
}
