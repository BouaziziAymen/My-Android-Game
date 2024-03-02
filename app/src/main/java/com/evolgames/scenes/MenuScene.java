package com.evolgames.scenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.particles.wrappers.FluxParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.MenuUserInterface;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends PhysicsScene<MenuUserInterface> {
  private FluxParticleWrapperWithPolygonEmitter flux;
  private GameGroup jarGroup;

  public MenuScene(Camera pCamera) {
    super(pCamera, SceneType.MENU);
  }

  @Override
  public void populate() {
    List<Vector2> vertices1 = VerticesFactory.createRectangle(240, 80);
    LayerProperties properties1 =
            PropertiesFactory.getInstance()
                    .createProperties(MaterialFactory.getInstance().getMaterialByIndex(2));
    LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1, 0);
    List<LayerBlock> blocks = new ArrayList<>();
    blocks.add(block1);

    GameGroup gameGroup1 = GameEntityFactory.getInstance()
            .createGameGroupTest(
                    blocks,
                    new Vector2(400 / 32f, 200 / 32f),
                    BodyDef.BodyType.StaticBody,
                    GroupType.OTHER);

    GameEntity gameEntity1 = gameGroup1.getGameEntityByIndex(0);
    gameEntity1.setCenter(new Vector2());
    gameEntity1.setName("Main Button");


//      gameEntity3.setCenter(new Vector2());
//      gameEntity1.setCenter(new Vector2());
//      this.flux = new FluxParticleWrapperWithPolygonEmitter(gameEntity3, gameEntity1);
//      this.attachChild(flux.getParticleSystem());

    createRagDoll(400,460);
    jarGroup = createItem("jar_latest.xml", 300, 320);
  }

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    super.onManagedUpdate(pSecondsElapsed);
    if(this.flux!=null) {
      this.flux.update();
    }
    if(step == 10){
      this.flux = new FluxParticleWrapperWithPolygonEmitter(jarGroup.getGameEntityByIndex(1), ragdoll.upperTorso);
      this.attachChild(flux.getParticleSystem());
    }
   if(step>10&&Math.random()<0.05f){
     jarGroup.getGameEntityByIndex(0).getBody().setAngularVelocity((Math.random()<0.5f?1:-1)*0.5f);
   }

  }

  @Override
  public void detach() {
    if(this.userInterface!=null){
      this.userInterface.detachSelf();
    }
  }
  public GameGroup createItem(float x, float y, ToolModel toolModel) {
    return createTool(toolModel,x,y);
  }
  @Override
  public void onPause() {
    this.detach();
  }

  @Override
  public void onResume() {
    createUserInterface();
  }

  @Override
  public void createUserInterface() {
    this.userInterface = new MenuUserInterface(this);
  }

  @Override
  protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
    userInterface.onTouchHud(hudTouchEvent);
  }

  public void goToScene(SceneType sceneType){
    ((MainScene)this.mParentScene).goToScene(sceneType);
  }
}
