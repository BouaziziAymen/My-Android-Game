package com.evolgames.entities.hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallelHandControl extends HandControl {

  private List<HandControl> controls;

  @SuppressWarnings("unused")
  public ParallelHandControl() {
  }
  public ParallelHandControl(HandControl... list) {
    super();
    this.controls = new ArrayList<>(Arrays.asList(list));
  }

  @Override
  public void run() {
    super.run();
    if (!isDead()) {
      for (HandControl handControl : controls) {
        handControl.run();
      }
    }
  }

  @Override
  public boolean isDead() {
    for (HandControl handControl : controls) {
      if (!handControl.isDead()) {
        return false;
      }
    }
    return true;
  }
}
