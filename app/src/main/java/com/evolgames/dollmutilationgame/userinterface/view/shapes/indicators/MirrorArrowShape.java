package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.strategy.TransformationStrategy;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.List;

public class MirrorArrowShape extends DoubleInvertedArrowsShape {

    private final TransformationStrategy transformationStrategy;

    public MirrorArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, EditorScene scene, boolean append, boolean invertShape) {
        super(begin, scene);

        this.transformationStrategy =
                new TransformationStrategy(shapePointsModel, append) {
                    @Override
                    protected boolean testPoints(List<Vector2> transformedPoints) {
                        return shapePointsModel.testPoints(transformedPoints);
                    }

                    @Override
                    protected List<Vector2> transformPoints() {
                        return transform(originalPoints);
                    }

                    private List<Vector2> transform(List<Vector2> points) {
                        return GeometryUtils.mirrorPoints(points, begin, end);
                    }

                    @Override
                    protected Vector2 transformCenter() {
                         return GeometryUtils.mirrorPoint(center, begin, end);
                    }
                };
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        transformationStrategy.transform();
    }
}
