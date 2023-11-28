package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Projectile extends Use implements Penetrating {

  public Projectile() {
    this.active = false;
  }

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {}

  @Override
  public PlayerSpecialAction getAction() {
    return null;
  }

  @Override
  public void onImpulseConsumed(
      WorldFacade worldFacade,
      Contact contact,
      Vector2 point,
      Vector2 normal,
      float actualAdvance,
      GameEntity penetrator,
      GameEntity penetrated,
      List<TopographyData> envData,
      List<TopographyData> penData,
      float consumedImpulse) {
    float massFraction =
        penetrator.getBody().getMass()
            / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());

    List<GameEntity> overlappedEntities =
        worldFacade.findOverlappingEntities(penData, envData, actualAdvance);

    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);

    Map<GameGroup, List<GameEntity>> groups =
        overlappedEntities.stream().collect(Collectors.groupingBy(GameEntity::getParentGroup));
    List<GameEntity> list =
        groups.values().stream()
            .map(e -> e.stream().findFirst().get())
            .collect(Collectors.toList());

    for (GameEntity overlappedEntity : list) {
      worldFacade.mergeEntities(
          overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), point.cpy());
      worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, overlappedEntity);
    }

    if (!list.isEmpty()) {
      worldFacade.freeze(penetrator);
      penetrator.setZIndex(-1);
      penetrator.getScene().sortChildren();
      setActive(false);
    }
  }

  @Override
  public void onFree(
      WorldFacade worldFacade,
      Contact contact,
      Vector2 point,
      Vector2 normal,
      float actualAdvance,
      GameEntity penetrator,
      GameEntity penetrated,
      List<TopographyData> envData,
      List<TopographyData> penData,
      float collisionImpulse) {
    float massFraction =
        penetrator.getBody().getMass()
            / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());
    worldFacade.destroyGameEntity(penetrator, true, false);
    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
    worldFacade.applyPointImpact(
        obtain(point), (float) (collisionImpulse * massFraction), penetrated);
    setActive(false);
  }

  @Override
  public void onCancel() {}
}
