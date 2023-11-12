package com.evolgames.entities.contact;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class ContactWrapper {

  public boolean continuous;
  public boolean dead;
  public LayerBlock block1;
  public LayerBlock block2;
  GameEntity entityA, entityB;
  int time;
  Fixture fixtureA, fixtureB;
  int numberOfContactPoints;
  Vector2[] points;
  float impulse;

  public ContactWrapper(Contact contact, int time, ContactImpulse impulse) {
    this.time = time;
    float[] nimp = impulse.getNormalImpulses();
    float[] timp = impulse.getTangentImpulses();
    this.impulse = (float) Math.sqrt(nimp[0] * nimp[0] + timp[0] * timp[0]);

    this.fixtureA = contact.getFixtureB();
    this.fixtureB = contact.getFixtureA();

    WorldManifold manifold = contact.getWorldManifold();
    this.numberOfContactPoints = manifold.getNumberOfContactPoints();

    this.points = new Vector2[this.numberOfContactPoints];
    for (int i = 0; i < this.numberOfContactPoints; i++)
      this.points[i] = Vector2Pool.obtain(manifold.getPoints()[i]);
    // for(int i = 0; i< this.numberOfContactPoints;
    // i++)Vector2Pool.recycle(manifold.getPoints()[i]);
    Body body1 = this.fixtureA.getBody();
    Body body2 = this.fixtureB.getBody();
    entityA = (GameEntity) body1.getUserData();
    entityB = (GameEntity) body2.getUserData();
    this.block1 = (LayerBlock) this.fixtureA.getUserData();
    this.block2 = (LayerBlock) this.fixtureB.getUserData();
    this.continuous = false;

    // Log.e("contact", "contact:"+entity1.ID+"/"+entity2.ID+"at"+World.onStep);
  }

  public float getImpulse() {
    return impulse;
  }

  public void setImpulse(float impulse) {
    this.impulse = impulse;
  }

  public Fixture getFixtureA() {
    return fixtureA;
  }

  public void setFixtureA(Fixture fixtureA) {
    this.fixtureA = fixtureA;
  }

  public Fixture getFixtureB() {
    return fixtureB;
  }

  public void setFixtureB(Fixture fixtureB) {
    this.fixtureB = fixtureB;
  }

  public boolean equivalent(ContactWrapper other) {
    if (entityA == other.entityA && entityB == other.entityB) return true;
    return entityA == other.entityB && entityB == other.entityA;
  }

  public GameEntity getEntityA() {
    return entityA;
  }

  public void setEntityA(GameEntity entityA) {
    this.entityA = entityA;
  }

  public GameEntity getEntityB() {
    return entityB;
  }

  public Vector2[] getPoints() {
    return points;
  }

  public void setPoints(Vector2[] pts) {
    this.points = new Vector2[this.numberOfContactPoints];
    for (int i = 0; i < this.numberOfContactPoints; i++)
      this.points[i] = Vector2Pool.obtain(pts[i]);
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getNumberOfContactPoints() {
    return numberOfContactPoints;
  }

  public void setNumberOfContactPoints(int numberOfContactPoints) {
    this.numberOfContactPoints = numberOfContactPoints;
  }
}
