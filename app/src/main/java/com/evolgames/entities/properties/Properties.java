package com.evolgames.entities.properties;

import android.support.annotation.NonNull;

public abstract class Properties implements Cloneable{
  @NonNull
  @Override
  public Object clone(){
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
