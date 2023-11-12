package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.blocks.LayerBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BlockQueryCallBack implements QueryCallback {
  private final List<LayerBlock> blocks = new ArrayList<>();
  private final List<Body> bodies = new ArrayList<>();

  public void reset() {
    blocks.clear();
    bodies.clear();
  }

  @Override
  public boolean reportFixture(Fixture fixture) {

    LayerBlock block = (LayerBlock) fixture.getUserData();
    if (block != null) {
      blocks.add(block);
      bodies.add(fixture.getBody());
    }
    return true;
  }

  public List<LayerBlock> getBlocks() {
    return blocks;
  }
  public List<Body> getBodies() {
    return bodies;
  }
}
