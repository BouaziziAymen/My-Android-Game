package com.evolgames.scenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.particles.wrappers.FluxParticleWrapper;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.MenuUserInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends PhysicsScene<MenuUserInterface> {
    private FluxParticleWrapper flux;
    private GameGroup jarGroup;

    public MenuScene(Camera pCamera) {
        super(pCamera, SceneType.MENU);
    }

    private void createEyes(){
        int[] playerAnimationFrames = {0, 1, 2, 3,4}; // indices of frames in spritesheet
        long[] playerFrameDurations = {800, 100, 150, 100, 100}; // duration of each frame in milliseconds
        AnimatedSprite playerSprite = new AnimatedSprite(0, 2, ResourceManager.getInstance().evilEyesTextureRegion, ResourceManager.getInstance().vbom);
       playerSprite.setScale(0.3f);
        playerSprite.animate(playerFrameDurations, playerAnimationFrames, true);
        ragdoll.head.getMesh().attachChild(playerSprite);
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
                        new Vector2(400 / 32f, 100 / 32f),
                        BodyDef.BodyType.StaticBody,
                        GroupType.OTHER);

        GameEntity gameEntity1 = gameGroup1.getGameEntityByIndex(0);
        gameEntity1.setName("Main Button");

        worldFacade.createFireSource(gameEntity1, new Vector2(120, 40), new Vector2(-120, 40), 100f, 0f, 1f, 0.2f, 0.1f, 1f, 1f, 1f);

        createRagDoll(415, 460);

        jarGroup = createItemFromFile("pandora's_jar#.xml", 300, 200, true, false);

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if (this.flux != null) {
            this.flux.update();
        }
        if (step == 60) {
            this.flux = new FluxParticleWrapper(jarGroup.getGameEntityByIndex(0), ragdoll.upperTorso);
            jarGroup.getGameEntityByIndex(0).setZIndex(5);
            flux.getParticleSystem().setZIndex(4);
            this.attachChild(flux.getParticleSystem());
            this.sortChildren();
        }
        if(step==90){
            createEyes();
        }
        if (step > 10 && Math.random() < 0.05f) {
            jarGroup.getGameEntityByIndex(0).getBody().setAngularVelocity((Math.random() < 0.5f ? 1 : -1) * 0.25f);
        }

    }

    @Override
    public void detach() {
        if (this.userInterface != null) {
            this.userInterface.detachSelf();
        }
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

    public void goToScene(SceneType sceneType) {
        ((MainScene) this.mParentScene).goToScene(sceneType);
    }
}
