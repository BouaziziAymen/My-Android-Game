package com.evolgames.entities.contact;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.evolgames.entities.basics.GameEntity;
import java.util.HashSet;

public class GameEntityContactListener implements ContactListener {
  private final ContactObserver observer;
  private final HashSet<Pair<GameEntity>> nonCollidingEntities;

  public GameEntityContactListener(ContactObserver observer) {
    this.observer = observer;
    this.nonCollidingEntities = new HashSet<>();
  }

  public void addNonCollidingPair(GameEntity entity1, GameEntity entity2) {
    if(shouldNotCollide(entity1,entity2)){
      return;
    }
    nonCollidingEntities.add(new Pair<>(entity1, entity2));
  }

  @Override
  public void beginContact(Contact contact) {
    if (shouldNotCollide(contact.getFixtureA(), contact.getFixtureB())) {
      contact.setEnabled(false);
      return;
    }
    observer.processImpactBeginContact(contact);
  }

  @Override
  public void endContact(Contact contact) {

    if (shouldNotCollide(contact.getFixtureA(), contact.getFixtureB())) {
      contact.setEnabled(false);
      return;
    }
    observer.processImpactEndContact(contact);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    if (shouldNotCollide(contact.getFixtureA(), contact.getFixtureB())) {
      contact.setEnabled(false);
      return;
    }
    observer.processImpactBeforeSolve(contact);
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
    if (shouldNotCollide(contact.getFixtureA(), contact.getFixtureB())) {
      contact.setEnabled(false);
      return;
    }
    observer.processImpactAfterSolve(contact, impulse);
  }

  public HashSet<Pair<GameEntity>> getNonCollidingEntities() {
    return nonCollidingEntities;
  }

  private boolean shouldNotCollide(Fixture fixture1, Fixture fixture2) {
    Body body1 = fixture1.getBody();
    Body body2 = fixture2.getBody();
    GameEntity entity1 = (GameEntity) body1.getUserData();
    GameEntity entity2 = (GameEntity) body2.getUserData();
    return shouldNotCollide(entity1, entity2);
  }

  public boolean shouldNotCollide(GameEntity entity1, GameEntity entity2) {
    for (Pair<GameEntity> pair : nonCollidingEntities) {
      if ((pair.first == entity1 && pair.second == entity2)
          || (pair.second == entity1 && pair.first == entity2)) {
        return true;
      }
    }
    return false;
  }

  public void removeNonCollidingPair(GameEntity entity1, GameEntity entity2) {
    nonCollidingEntities.removeIf(
        pair ->
            (pair.first == entity1 && pair.second == entity2)
                || (pair.second == entity1 && pair.first == entity2));
  }
}
