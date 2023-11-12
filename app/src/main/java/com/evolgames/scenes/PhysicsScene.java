package com.evolgames.scenes;

import android.content.Context;
import android.content.SharedPreferences;

import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.UserInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class PhysicsScene<T extends UserInterface<?>> extends AbstractScene<T> {

  protected final WorldFacade worldFacade;
  protected final List<GameGroup> gameGroups = new ArrayList<>();
  protected final HashMap<Integer, Hand> hands = new HashMap<>();

  public PhysicsScene(Camera pCamera, SceneType sceneName) {
    super(pCamera, sceneName);
    this.worldFacade = new WorldFacade(this);
    BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
    GameEntityFactory.getInstance().create(this.worldFacade.getPhysicsWorld(), this);
    BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
    Invoker.setScene(this);
  }

  public WorldFacade getWorldFacade() {
    return worldFacade;
  }

  public PhysicsWorld getPhysicsWorld() {
    return getWorldFacade().getPhysicsWorld();
  }

  @Override
  public void populate() {}

  @Override
  public void detach() {}

  @Override
  public void onPause() {}

  @Override
  public void onResume() {}

  public List<GameGroup> getGameGroups() {
    return gameGroups;
  }

  public void addGameGroup(GameGroup gameGroup) {
    gameGroups.add(gameGroup);
  }

  public HashMap<Integer, Hand> getHands() {
    return hands;
  }

  public void setMouseJoint(MouseJoint joint, GameEntity gameEntity) {
    if (hands.get(gameEntity.getHangedPointerId()) != null) {
      Objects.requireNonNull(hands.get(gameEntity.getHangedPointerId()))
          .setMouseJoint(joint, gameEntity);
    }
  }

  public void onDestroyMouseJoint(MouseJoint j) {
    Optional<Hand> hand = hands.values().stream().filter(e -> e.getMouseJoint() == j).findFirst();
    hand.ifPresent(Hand::onMouseJointDestroyed);
  }

  public GameEntity getGameEntityByUniqueId(String uniqueId){
    for(GameGroup gameGroup:gameGroups){
      for(GameEntity gameEntity:gameGroup.getGameEntities()){
        if(gameEntity.getUniqueID().equals(uniqueId)){
          return gameEntity;
        }
      }
    }
    return null;
  }
}
