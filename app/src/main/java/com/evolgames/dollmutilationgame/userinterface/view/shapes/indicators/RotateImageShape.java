package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.ImageShape;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

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
