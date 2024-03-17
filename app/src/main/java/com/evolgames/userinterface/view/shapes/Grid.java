package com.evolgames.userinterface.view.shapes;

import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.AbstractScene;

import org.andengine.entity.primitive.LineChain;
import org.andengine.entity.primitive.LineStrip;

public class Grid {

    private final AbstractScene<?> editorScene;
    private LineChain lineChain;

    public Grid(AbstractScene<?> editorScene) {
        this.editorScene = editorScene;
        update();
    }

    private void update() {
        if (lineChain != null) lineChain.detachSelf();
        this.lineChain = new LineStrip(0, 0, 2, 1000, ResourceManager.getInstance().vbom);
        lineChain.setZIndex(-1);
        editorScene.attachChild(lineChain);
        editorScene.sortChildren();
        float y = 0;
        final float s = 32f;
        float h = GameActivity.CAMERA_HEIGHT;
        float w = GameActivity.CAMERA_WIDTH;
        while (y <= h) {
            lineChain.add(0, y);
            lineChain.add(w, y);
            lineChain.add(w, y + s);
            lineChain.add(0, y + s);
            y += 2 * s;
        }
        float x = 0;
        while (x <= w) {
            lineChain.add(x, y);
            lineChain.add(x, 0);
            lineChain.add(x + s, 0);
            lineChain.add(x + s, y);
            x += 2 * s;
        }
    }
}
