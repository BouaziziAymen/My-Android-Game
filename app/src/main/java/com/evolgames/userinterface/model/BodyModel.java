package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.BodyUsageCategory;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.ImpactBomb;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.Missile;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyModel extends OutlineModel<BodyProperties> {
  public static final ArrayList<BodyUsageCategory> allCategories =
      new ArrayList<>(EnumSet.allOf(BodyUsageCategory.class));
  private final AtomicInteger layerCounter = new AtomicInteger();
  private final int bodyId;
  private final ArrayList<LayerModel> layers;
  private final ArrayList<ProjectileModel> projectileModels;
  private final ArrayList<CasingModel> casingModels;
  private final ArrayList<DragModel> dragModels;
  private final ArrayList<FireSourceModel> fireSourceModels;
  private final ArrayList<LiquidSourceModel> liquidSourceModels;
  private final List<UsageModel<?>> usageModels;
  private GameEntity gameEntity;
  private final List<BombModel> bombModels;
  private BodyField field;
  private boolean bullet;

  public BodyModel(int bodyId) {
    super("Body" + bodyId);
    this.bodyId = bodyId;
    layers = new ArrayList<>();
    projectileModels = new ArrayList<>();
    casingModels = new ArrayList<>();
    properties = new BodyProperties();
    usageModels = new ArrayList<>();
    bombModels = new ArrayList<>();
    dragModels = new ArrayList<>();
    fireSourceModels = new ArrayList<>();
    liquidSourceModels = new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public <T extends Properties> T getUsageModelProperties(BodyUsageCategory bodyUsageCategory) {
    return ((UsageModel<T>)
            usageModels.stream().filter(e -> e.getType() == bodyUsageCategory).findFirst().get())
        .getProperties();
  }

  @SuppressWarnings("unchecked")
  public <T extends Properties> UsageModel<T> getUsageModel(BodyUsageCategory bodyUsageCategory) {
    return ((UsageModel<T>)
        usageModels.stream().filter(e -> e.getType() == bodyUsageCategory).findFirst().get());
  }

  public void onChildLayerOutlineUpdated(int layerId) {}

  public GameEntity getGameEntity() {
    return gameEntity;
  }

  public void setGameEntity(GameEntity gameEntity) {
    this.gameEntity = gameEntity;
  }

  void swapLayers(int index1, int index2) {
    Collections.swap(layers, index1, index2);
  }

  LayerModel createLayer() {
    LayerModel layerModel = new LayerModel(bodyId, layerCounter.getAndIncrement(), this);
    layers.add(layerModel);
    return layerModel;
  }

  LayerModel getLayerModelById(int layerId) {
    for (LayerModel layer : layers) {
      if (layer.getLayerId() == layerId) return layer;
    }
    return null;
  }

  public CasingModel getCasingModelById(int ammoId) {
    for (CasingModel ammoModel : casingModels) {
      if (ammoModel.getCasingId() == ammoId) return ammoModel;
    }
    return null;
  }

  public int getBodyId() {
    return bodyId;
  }

  public ArrayList<LayerModel> getLayers() {
    return layers;
  }

  DecorationModel createNewDecoration(int layerId) {
    return Objects.requireNonNull(getLayerModelById(layerId)).createDecoration();
  }

  public String toString() {
    String s = "Body" + bodyId + ": \n";
    for (int i = 0; i < layers.size(); i++) {
      s += layers.get(i).toString() + "\n";
    }
    return s;
  }

  void removeLayer(int layerId) {
    layers.remove(getLayerModelById(layerId));
  }

  public DecorationModel removeDecoration(int layerId, int decorationId) {
    return getLayerModelById(layerId).removeDecoration(decorationId);
  }

  public AtomicInteger getLayerCounter() {
    return layerCounter;
  }

  public Vector2 getCenter() {
    List<List<Vector2>> list = new ArrayList<>();
    for (LayerModel layerModel : layers) {
      list.add(layerModel.getPoints());
    }
    return GeometryUtils.calculateCenter(list);
  }

  @Override
  public void updateOutlinePoints() {
    throw new UnsupportedOperationException();
  }

  public List<CasingModel> getCasingModels() {
    return casingModels;
  }

  public List<UsageModel<?>> getUsageModels() {
    return usageModels;
  }

  public ArrayList<ProjectileModel> getProjectileModels() {
    return projectileModels;
  }

  public List<BombModel> getBombModels() {
    return bombModels;
  }

  public ArrayList<DragModel> getDragModels() {
    return dragModels;
  }

  public BombModel getBombModelById(int bombId) {
    return bombModels.stream()
        .filter(bombModel -> bombModel.getBombId() == bombId)
        .findFirst()
        .orElse(null);
  }

  public BodyField getField() {
    return field;
  }

  public void setField(BodyField bodyField) {
    this.field = bodyField;
  }

  public ArrayList<FireSourceModel> getFireSourceModels() {
    return fireSourceModels;
  }

  public ArrayList<LiquidSourceModel> getLiquidSourceModels() {
    return liquidSourceModels;
  }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }




    public void setupUsages(PhysicsScene<?> physicsScene, boolean mirrored){
                      this.getUsageModels()
                              .forEach(
                                      e -> {
                                        switch (e.getType()){
                                          case SHOOTER:
                                          case SHOOTER_CONTINUOUS:
                                            Shooter shooter = new Shooter(e, physicsScene,mirrored);
                                            this.getGameEntity().getUseList().add(shooter);
                                            break;
                                          case TIME_BOMB:
                                            TimeBomb timeBomb = new TimeBomb(e,mirrored);
                                            this.getGameEntity().getUseList().add(timeBomb);
                                            break;
                                          case FUZE_BOMB:
                                            break;
                                          case IMPACT_BOMB:
                                            ImpactBomb impactBomb = new ImpactBomb(e,mirrored);
                                            this.getGameEntity().getUseList().add(impactBomb);
                                            break;
                                          case SLASHER:
                                            Slasher slasher = new Slasher();
                                            this.getGameEntity().getUseList().add(slasher);
                                            break;
                                          case BLUNT:
                                            Smasher smasher = new Smasher();
                                            this.getGameEntity().getUseList().add(smasher);
                                            break;
                                          case STABBER:
                                            Stabber stabber = new Stabber();
                                            this.getGameEntity().getUseList().add(stabber);
                                            break;
                                          case THROWING:
                                            Throw throwable = new Throw();
                                            this.getGameEntity().getUseList().add(throwable);
                                            break;
                                          case FLAME_THROWER:
                                            FlameThrower flameThrower = new FlameThrower(e, physicsScene,mirrored);
                                            this.getGameEntity().getUseList().add(flameThrower);
                                            break;
                                          case ROCKET:
                                            Rocket rocket = new Rocket(e, physicsScene,mirrored);
                                            rocket.setRocketBodyGameEntity(this.getGameEntity());
                                            this.getGameEntity().getUseList().add(rocket);
                                            break;
                                          case MISSILE:
                                            Missile missile = new Missile(e, physicsScene,mirrored);
                                            missile.setRocketBodyGameEntity(this.getGameEntity());
                                            this.getGameEntity().getUseList().add(missile);
                                            break;
                                          case LIQUID_CONTAINER:
                                            LiquidContainer liquidContainer = new LiquidContainer(e, physicsScene,mirrored);
                                            this.getGameEntity().getUseList().add(liquidContainer);
                                            break;
                                          case ROCKET_LAUNCHER:
                                            RocketLauncher rocketLauncher = new RocketLauncher(e, physicsScene.getWorldFacade(),mirrored);
                                            this.getGameEntity().getUseList().add(rocketLauncher);
                                            break;
                                        }

                                      });


    }



}
