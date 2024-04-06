package com.evolgames.activity;

import org.andengine.audio.sound.Sound;

public class GameSound {
    private final SoundType soundType;

    public enum SoundType{
        PROJECTILE, PENETRATION, EXPLOSION
    }
    private final Sound sound;
    private final String title;

    public GameSound(Sound sound, String title, SoundType soundType) {
        this.sound = sound;
        this.title = title;
        this.soundType = soundType;
        this.sound.setVolume(1f);
    }

    public Sound getSound() {
        return sound;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public String getTitle() {
        return title;
    }
}
