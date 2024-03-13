package com.evolgames.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import java.util.ArrayList;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class LinearRayCastCallback implements RayCastCallback {
  private final ArrayList<Fixture> coveredFixtures;
  private final ArrayList<ArrayList<Flag>> flags;
  private final ArrayList<LayerBlock> coveredBlocks;
  private final ArrayList<GameEntity> coveredEntities;
  private boolean direction = true;
  private GameEntity exclusive;
  private GameEntity excepted;

  LinearRayCastCallback() {
    flags = new ArrayList<>();
    coveredBlocks = new ArrayList<>();
    coveredFixtures = new ArrayList<>();
    coveredEntities = new ArrayList<>();
  }

  public void setExcepted(GameEntity excepted) {
    this.excepted = excepted;
  }

  public void setExclusive(GameEntity exclusive) {
    this.exclusive = exclusive;
  }

  public ArrayList<ArrayList<Flag>> getFlags() {
    return flags;
  }

  public ArrayList<LayerBlock> getCoveredBlocks() {
    return coveredBlocks;
  }

  public boolean isDirection() {
    return direction;
  }

  public void setDirection(boolean direction) {
    this.direction = direction;
  }

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    if (fixture.isSensor()) return 1;
    GameEntity entity = (GameEntity) fixture.getBody().getUserData();
    if (exclusive != null && entity != exclusive) return 1;
    if (excepted != null && entity == excepted) return 1;

    LayerBlock block = (LayerBlock) fixture.getUserData();
    Vector2 Point = Vector2Pool.obtain(point);
    FlagType type = (direction) ? FlagType.Entering : FlagType.Leaving;
    if (coveredBlocks.contains(block)) {
      int index = coveredBlocks.indexOf(block);
      flags.get(index).add(new Flag(block, Point, fraction, type));

    } else {
      ArrayList<Flag> list = new ArrayList<>();
      list.add(new Flag(block, Point, fraction, type));
      flags.add(list);
      coveredBlocks.add(block);
      coveredFixtures.add(fixture);
      coveredEntities.add(entity);
    }

    return 1;
  }

  public ArrayList<GameEntity> getCoveredEntities() {
    return coveredEntities;
  }

  public void reset() {
    excepted = null;
    exclusive = null;
    coveredBlocks.clear();
    coveredEntities.clear();
    flags.clear();
  }

  public ArrayList<Fixture> getCoveredFixtures() {
    return coveredFixtures;
  }
}
