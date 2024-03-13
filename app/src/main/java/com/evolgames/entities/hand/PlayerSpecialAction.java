package com.evolgames.entities.hand;

import com.evolgames.gameengine.R;

public enum PlayerSpecialAction {
  None(R.drawable.drag_icon,false,false),
  Trigger(R.drawable.tap_icon,false,false),
  SwitchOn(R.drawable.on_icon,true,false),
  SwitchOff(R.drawable.off_icon,true,false),
  Slash(R.drawable.slash_icon,false,true),
  Stab(R.drawable.stab_icon,false,true),
  Throw(R.drawable.hatchet_icon,false,true),
  Smash(R.drawable.mace_icon,false,true),
  Fire(R.drawable.shooting_icon,false,true),
  Guide(R.drawable.home_icon,false,true);

  public final int iconId;
  public final boolean doubleState;
  public final boolean selectableByDefault;

  PlayerSpecialAction(int iconId, boolean doubleState, boolean selectableByDefault) {
    this.iconId = iconId;this.doubleState=doubleState;this.selectableByDefault = selectableByDefault;
  }
}
