package com.evolgames.entities.particles.persistence;

public class PersistenceException extends Exception {
  private PersistenceException parent;

  public PersistenceException(String message) {
    super(message);
  }

  public PersistenceException(String message, PersistenceException parent) {
    super(message);
    this.parent = parent;
  }

  public PersistenceException getParent() {
    return parent;
  }
}
