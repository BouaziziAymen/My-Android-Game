package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TextureRegion;

public class WeldJointShape extends JointShape {
    public WeldJointShape(UserInterface userInterface, Vector2 begin, GameScene scene) {
        super(userInterface, begin, scene, ResourceManager.getInstance().emptySquareTextureRegion);
    }
}
