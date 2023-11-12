package com.evolgames.entities.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;

public interface ContactObserver {
  void processImpactAfterSolve(Contact contact, ContactImpulse impulse);

  void processImpactBeginContact(Contact contact);

  void processImpactEndContact(Contact contact);

  void processImpactBeforeSolve(Contact contact);
}
