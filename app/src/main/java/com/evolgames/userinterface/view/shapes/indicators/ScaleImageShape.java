package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.ImageShapeModel;
import com.evolgames.userinterface.view.shapes.ImageShape;

public class ScaleImageShape extends ArrowShape {

    private final ImageShape imageShape;
    private final boolean fixedRatio;

    public ScaleImageShape(Vector2 begin, ImageShape imageShape, EditorScene scene, boolean fixedRatio) {
        super(begin, scene);
        this.imageShape = imageShape;
        this.fixedRatio = fixedRatio;
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        float[] post = imageShape.getSprite().convertSceneCoordinatesToLocalCoordinates(x, y);

        float disX = post[0] > 0 ? post[0] : 0;
        float disY = post[1] > 0 ? post[1] : 1;
        ImageShapeModel imageModel = creationScene.getUserInterface().getToolModel().getImageShapeModel();
        if (!fixedRatio) {
            imageShape.updateWidth(disX);
            imageShape.updateHeight(disY);
        } else {
            imageShape.updateWidthWithRatio(disX);
        }
        creationScene.getUserInterface().getOptionsWindowController().onUpdatedImageDimensions(imageShape);
        imageShape.updateSelf();
    }
}
