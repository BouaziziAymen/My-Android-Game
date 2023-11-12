package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.EntityWithBody;
import com.evolgames.entities.serialization.InitInfo;

public class BodyInitImpl implements BodyInit {

  private static short groupNumber;
  private Filter filter;

  @SuppressWarnings("unused")
  public BodyInitImpl() {}

  public BodyInitImpl(short category) {
    this.filter = new Filter();
    this.filter.categoryBits = category;
    this.filter.maskBits = category;
    this.filter.groupIndex = (short) - ++groupNumber;
  }

  public BodyInitImpl(Filter filter) {
    this.filter = filter;
  }

  @Override
  public void initialize(Body body) {
    EntityWithBody entity = (EntityWithBody) body.getUserData();
    entity.setGroupIndex(this.filter.groupIndex);
    for (Fixture f : body.getFixtureList()) {
      f.setFilterData(this.filter);
    }
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  @Override
  public InitInfo getInitInfo(InitInfo initInfo) {
    initInfo.setFilter(filter);
    return initInfo;
  }
}
