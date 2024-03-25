package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.view.shapes.ImageShape;
import com.evolgames.utilities.GeometryUtils;

public class RotateImageShape extends FixedLengthArrowShape {

    private final ImageShape imageShape;

    public RotateImageShape(Vector2 begin, ImageShape imageShape, EditorScene scene, float length) {
        super(begin, scene, length);
        this.imageShape = imageShape;
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        float angle = (float) Math.atan2(-direction.y, direction.x) * GeometryUtils.TO_DEGREES;
        imageShape.updateRotation(angle);
        imageShape.updateSelf();
        creationScene.getUserInterface().getOptionsWindowController().onUpdatedImageDimensions(imageShape);
    }
}
