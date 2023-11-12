package com.evolgames.entities.observer;

public interface Observer<T extends Subject> {
  void update(T observable);
}
