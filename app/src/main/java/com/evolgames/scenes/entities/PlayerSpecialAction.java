package com.evolgames.scenes.entities;

public enum PlayerSpecialAction {
  None(7),
  Grenade(0),
  Slash(6),
  Stab(4),
  Throw(2),
  Smash(5),
  Shoot(3);
  public final int iconId;

  PlayerSpecialAction(int iconId) {
    this.iconId = iconId;
  }
}
