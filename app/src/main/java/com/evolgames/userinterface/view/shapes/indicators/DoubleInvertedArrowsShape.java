package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;

import org.andengine.entity.primitive.LineStrip;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class DoubleInvertedArrowsShape extends LineShape {
    public DoubleInvertedArrowsShape(Vector2 begin, EditorScene scene) {
        super(begin, scene);
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        if (lineStrip != null) {
            lineStrip.detachSelf();
        }
        this.lineStrip = new LineStrip(0, 0, 2, 100, ResourceManager.getInstance().vbom);
        lineStrip.setZIndex(2);
        creationScene.sortChildren();
        creationScene.attachChild(lineStrip);
        creationScene.sortChildren();

        float nx = -direction.y;
        float ny = direction.x;
        Vector2 n = Vector2Pool.obtain(nx, ny);

        Vector2 q = Vector2Pool.obtain(begin).sub(direction.x * 16, direction.y * 16);
        Vector2 q1 = Vector2Pool.obtain(q).add(n.x * 6, n.y * 6);
        Vector2 q2 = Vector2Pool.obtain(q).sub(n.x * 6, n.y * 6);
        lineStrip.add(begin.x, begin.y);
        lineStrip.add(q1.x, q1.y);
        lineStrip.add(q2.x, q2.y);

        lineStrip.add(begin.x, begin.y);
        lineStrip.add(end.x, end.y);

        Vector2 p = Vector2Pool.obtain(end).add(direction.x * 16, direction.y * 16);
        Vector2 p1 = Vector2Pool.obtain(p).add(n.x * 6, n.y * 6);
        Vector2 p2 = Vector2Pool.obtain(p).sub(n.x * 6, n.y * 6);
        lineStrip.add(p1.x, p1.y);
        lineStrip.add(p2.x, p2.y);
        lineStrip.add(end.x, end.y);

        Vector2Pool.recycle(p);
        Vector2Pool.recycle(p1);
        Vector2Pool.recycle(p2);
        Vector2Pool.recycle(q);
        Vector2Pool.recycle(q1);
        Vector2Pool.recycle(q2);
        Vector2Pool.recycle(direction);
        Vector2Pool.recycle(n);
    }
}
