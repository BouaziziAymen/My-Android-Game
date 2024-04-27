package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.scenes.EditorScene;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class FixedLengthArrowShape extends ArrowShape {
    protected float length;

    FixedLengthArrowShape(Vector2 begin, EditorScene scene, float length) {
        super(begin, scene);
        this.length = length;
    }

    FixedLengthArrowShape(Vector2 begin, EditorScene scene, float length, int size) {
        super(begin, scene, size);
        this.length = length;
    }

    @Override
    public void updateEnd(float x, float y) {
        direction = Vector2Pool.obtain(x, y).cpy().sub(begin).nor();
        Vector2 v = Vector2Pool.obtain(direction).mul(length);
        setEnd(begin.x + v.x, begin.y + v.y);
    }

    @Override
    public void updateBegin(float x, float y) {
        Vector2 v = Vector2Pool.obtain(direction).mul(length);
        super.updateBegin(x, y);
        setEnd(begin.x + v.x, begin.y + v.y);
    }
}
