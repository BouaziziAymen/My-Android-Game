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
import com.evolgames.entities.particles.wrappers.SegmentExplosiveParticleWrapper;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.scenes.entities.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends PhysicsScene {
    private FluxParticleWrapper flux;
    private GameGroup jarGroup;
    private SegmentExplosiveParticleWrapper fireSourece;

    public MenuScene(Camera pCamera) {
        super(pCamera, SceneType.MENU);
    }

    private void createEyes() {
        int[] playerAnimationFrames = {0, 1, 2, 3, 4}; // indices of frames in spritesheet
        long[] playerFrameDurations = {800, 100, 150, 100, 100}; // duration of each frame in milliseconds
        AnimatedSprite playerSprite = new AnimatedSprite(0, 2, ResourceManager.getInstance().evilEyesTextureRegion, ResourceManager.getInstance().vbom);
        playerSprite.setScale(0.3f);
        playerSprite.animate(playerFrameDurations, playerAnimationFrames, true);
        ragdoll.head.getMesh().attachChild(playerSprite);
    }

    @Override
    public void populate() {
        Rectangle buttonBounds = new Rectangle(400, 100, 240, 80, ResourceManager.getInstance().vbom);
        buttonBounds.setColor(Color.TRANSPARENT);
        attachChild(buttonBounds);
        registerTouchArea(buttonBounds);
        Sprite playText = new Sprite(400, 100, ResourceManager.getInstance().playTextureRegion, ResourceManager.getInstance().vbom);
        playText.setZIndex(2);
        attachChild(playText);

        setOnSceneTouchListener((pScene, pSceneTouchEvent) -> {
            if (pSceneTouchEvent.isActionDown()) {
                float touchX = pSceneTouchEvent.getX();
                float touchY = pSceneTouchEvent.getY();
                if (buttonBounds.contains(touchX, touchY)) {

                    IEntityModifier modifier = new SequenceEntityModifier(
                            new ScaleModifier(0.1f, 1.0f, 1.2f),
                            new ScaleModifier(0.1f, 1.2f, 1.0f)
                    );
                    playText.registerEntityModifier(modifier);

                    // Perform event 'x' after animation completes
                    modifier.addModifierListener(new IEntityModifier.IEntityModifierListener() {
                        @Override
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                            ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().playSound,1f,5);
                        }

                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                            goToScene(SceneType.PLAY);
                        }
                    });
                    return true; // Consume the touch event
                }
            }
            return false; // Allow other touch events to be processed
        });

        List<Vector2> vertices = VerticesFactory.createRectangle(240, 80);
        LayerProperties properties =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(2));
        LayerBlock block = BlockFactory.createLayerBlock(vertices, properties, 1, 0);
        List<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block);

        GameGroup buttonGroup = GameEntityFactory.getInstance()
                .createGameGroupTest(
                        blocks,
                        new Vector2(400 / 32f, 100 / 32f),
                        BodyDef.BodyType.StaticBody,
                        GroupType.OTHER);

        GameEntity playButtonEntity = buttonGroup.getGameEntityByIndex(0);
        playButtonEntity.setName("Main Button");

        this.fireSourece = worldFacade.createFireSource(playButtonEntity, new Vector2(120, 40), new Vector2(-120, 40), 100f, 0f, 1f, 0.2f, 0.1f, 1f, 1f, 1f);

        createRagDoll(415, 460);

        jarGroup = createItemFromFile("pandora's_jar#.xml", 300, 200, true, false);

        sortChildren();
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
        if (step == 10) {
            createEyes();
        }
        if (step > 10 && Math.random() < 0.05f) {
            jarGroup.getGameEntityByIndex(0).getBody().setAngularVelocity((Math.random() < 0.5f ? 1 : -1) * 0.25f);
        }

    }

    @Override
    public void detach() {
       super.detach();
        if(fireSourece!=null){
            fireSourece.detach();
        }
      if(flux!=null) {
          flux.detach();
      }
        System.gc();
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

    }

    public void goToScene(SceneType sceneType) {
        ((MainScene) this.mParentScene).goToScene(sceneType);
    }
}
