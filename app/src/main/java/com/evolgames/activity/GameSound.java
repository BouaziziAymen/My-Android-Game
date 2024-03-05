package com.evolgames.activity;

import java.util.List;
import org.andengine.audio.sound.Sound;

public class GameSound {
  private final List<Sound> soundList;
  private final String title;

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
