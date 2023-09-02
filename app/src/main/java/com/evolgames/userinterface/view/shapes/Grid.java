package com.evolgames.userinterface.view.shapes;

import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;

import org.andengine.entity.primitive.LineChain;
import org.andengine.entity.primitive.LineStrip;


public class Grid {

    private LineChain lineChain;
    private final GameScene gameScene;

    public Grid(GameScene gameScene) {
        this.gameScene = gameScene;
        update();
    }

    private void update() {
        if (lineChain != null) lineChain.detachSelf();
        this.lineChain = new LineStrip(0, 0, 2, 1000, ResourceManager.getInstance().vbom);
        lineChain.setZIndex(-1);
        gameScene.attachChild(lineChain);
        gameScene.sortChildren();
        float y = 0;
        float s = 32f;
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
