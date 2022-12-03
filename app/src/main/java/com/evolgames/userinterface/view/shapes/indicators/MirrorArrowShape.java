package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.shapes.indicators.strategy.TransformationStrategy;

import java.util.ArrayList;
import java.util.List;

public class MirrorArrowShape extends DoubleInvertedArrowsShape {

    private final TransformationStrategy transformationStrategy;

    public MirrorArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, List<Vector2> copiedPoints, GameScene scene) {
        super(begin, scene);

        this.transformationStrategy = new TransformationStrategy(shapePointsModel, copiedPoints) {
            @Override
            protected boolean testPoints(List<Vector2> transformedPoints) {
                return shapePointsModel.test(transformedPoints);
            }

            @Override
            protected List<Vector2> transformPoints(List<Vector2> originalPoints) {
                return GeometryUtils.mirrorPoints(originalPoints, begin, end);
            }
        };
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        transformationStrategy.transform();

    }

}
