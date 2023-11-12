package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;

public class RadianceRayCastCallBack implements RayCastCallback {

  public Vector2 point;
  private LayerBlock block;
  private GameEntity excepted;
  private Vector2 intersectionPoint;
  private float minfraction;

  /*


      @Override
      public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
          if(fixture.getBody().getType()==BodyDef.BodyType.StaticBody)return 0;
          GameEntity entity = (GameEntity) fixture.getBody().getUserData();

          this.block = (BlockA) fixture.getUserData();
          Vector2 reflectionPoint=	entity.getBody().getLocalPoint(point).cpy().mul(32f);

          if(!Utils.isOnBorder(reflectionPoint, this.block.getVertices()))return 0;


          CoatingBlock nearest = this.block.getBlockGrid().getNearestCoatingBlockSimple(reflectionPoint);
          if(this.temperature >nearest.getTemperature())nearest.applyDeltaTemperature((this.temperature -nearest.getTemperature())/PhysicsConstants.RADIANCE_CONSTANT);
  GameScene.plotter.drawLine2(center.cpy().mul(32),point.cpy().mul(32),Color.RED,1);
         // GameScene.plotter.drawPoint(point.cpy().mul(32), Color.RED, 1, 0);
          return 0;
      }

       */
  public LayerBlock getBlock() {
    return this.block;
  }

  public Vector2 getIntersectionPoint() {
    return intersectionPoint;
  }

  public void reset() {
    intersectionPoint = null;
    minfraction = Float.MAX_VALUE;
    block = null;
    point = null;
    excepted = null;
  }

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    GameEntity candidate = (GameEntity) fixture.getBody().getUserData();
    if (candidate.getName().equals("Ground") || candidate == excepted) return 1;

    if (fraction < minfraction) {
      this.block = (LayerBlock) fixture.getUserData();
      this.point = point.cpy();
      intersectionPoint = candidate.getBody().getLocalPoint(point).cpy().mul(32f);
      minfraction = fraction;
    }

    return 1;
  }

  public float getFraction() {
    return minfraction;
  }

  public GameEntity getExcepted() {
    return excepted;
  }

  public void setExcepted(GameEntity excepted) {
    this.excepted = excepted;
  }
}
