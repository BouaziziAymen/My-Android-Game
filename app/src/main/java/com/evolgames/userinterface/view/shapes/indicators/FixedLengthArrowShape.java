package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.shapes.indicators.ArrowShape;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class FixedLengthArrowShape extends ArrowShape {
    protected float length;
     FixedLengthArrowShape(Vector2 begin, GameScene scene, float length) {
        super(begin, scene);
        this.length = length;
    }
    FixedLengthArrowShape(Vector2 begin, GameScene scene, float length, int size) {
        super(begin, scene,size);
        this.length = length;
    }

    @Override
    public void setEnd(float x, float y) {
        dir = Vector2Pool.obtain(x,y).cpy().sub(begin).nor();
        Vector2 v = Vector2Pool.obtain(dir).mul(length);
        super.setEnd(begin.x+v.x,begin.y+v.y);
    }

    @Override
    public void setBegin(float x, float y) {
        Vector2 v = Vector2Pool.obtain(dir).mul(length);
        super.setBegin(x, y);
        super.setEnd(begin.x+v.x,begin.y+v.y);

    }
}
