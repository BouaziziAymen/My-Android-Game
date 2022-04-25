package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.shapes.ImageShape;

public class RotateImageShape extends FixedLengthArrowShape {


    private final ImageShape imageShape;

    public RotateImageShape(Vector2 begin, ImageShape imageShape, GameScene scene, float length) {
        super(begin, scene, length);
        this.imageShape = imageShape;
    }

    @Override
    public void onUpdated(float x, float y) {
        super.onUpdated(x, y);
        float angle = (float) Math.atan2(-dir.y, dir.x) * GeometryUtils.TO_DEGREES;
        imageShape.getSprite().setRotation(angle);
        imageShape.updateSelf();
    }



}

