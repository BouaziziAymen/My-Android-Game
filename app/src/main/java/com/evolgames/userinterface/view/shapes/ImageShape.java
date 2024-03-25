package com.evolgames.userinterface.view.shapes;

import android.graphics.Bitmap;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.ImageShapeModel;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.MyColorUtils;

import org.andengine.entity.primitive.LineLoop;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.transformation.Transformation;

import java.util.ArrayList;

public class ImageShape extends Container {
    private final CircleShape pipeCircle;
    private final EditorScene creationScene;
    private final ImageShapeModel imageShapeModel;
    private final float ratio;
    private Sprite sketchSprite;
    private LineLoop lineLoop;
    private ArrayList<Vector2> points;

    public ImageShape(EditorScene scene, ImageShapeModel imageShapeModel) {
        creationScene = scene;
        TextureRegion region = ResourceManager.getInstance().sketchTextureRegion;
        sketchSprite =
                new Sprite(
                        region.getWidth(),
                        region.getHeight(),
                        ResourceManager.getInstance().sketchTextureRegion,
                        ResourceManager.getInstance().vbom);
        creationScene.attachChild(sketchSprite);
        sketchSprite.setZIndex(-1);
        creationScene.sortChildren();
        setDepth(-10);
        updateSelf();
        this.pipeCircle = new CircleShape(sketchSprite);
        this.imageShapeModel = imageShapeModel;
        this.pipeCircle.setVisible(false);
        this.ratio = region.getHeight() / region.getWidth();
    }

    public void updateSprite() {
        if (sketchSprite == null) {
            return;
        }
        TextureRegion region = ResourceManager.getInstance().sketchTextureRegion;
        sketchSprite.detachSelf();
        sketchSprite =
                new Sprite(
                        region.getWidth(),
                        region.getHeight(),
                        ResourceManager.getInstance().sketchTextureRegion,
                        ResourceManager.getInstance().vbom);
        creationScene.attachChild(sketchSprite);
        sketchSprite.setZIndex(-1);
        creationScene.sortChildren();
    }

    public Sprite getSprite() {
        return sketchSprite;
    }

    public void setPipeCircleVisibility(boolean visible) {
        pipeCircle.setVisible(visible);
    }

    public void setPipeCircleRadius(int radius) {
        pipeCircle.setRadius(radius);
    }

    private void updateBounds() {
        float x = sketchSprite.getX();
        float y = sketchSprite.getY();
        float halfWidth = sketchSprite.getWidth() / 2;
        float halfHeight = sketchSprite.getHeight() / 2;
        float[] data = new float[12];
        data[0] = halfWidth + 8;
        data[1] = halfHeight + 8;
        data[2] = halfWidth;
        data[3] = halfHeight;
        data[4] = halfWidth;
        data[5] = -halfHeight;
        data[6] = -halfWidth;
        data[7] = -halfHeight;
        data[8] = -halfWidth;
        data[9] = halfHeight;
        data[10] = halfWidth;
        data[11] = halfHeight;

        Transformation transform = GeometryUtils.transformation;
        transform.setToIdentity();
        transform.preTranslate(x, y);
        transform.preRotate(-sketchSprite.getRotation());
        transform.transform(data);
        points = new ArrayList<>();
        for (int i = 0; i < 12; i += 2) {
            points.add(new Vector2(data[i], data[i + 1]));
        }
    }

    public void setLineLoopColor(float r, float g, float b) {
        if (lineLoop != null) lineLoop.setColor(r, g, b);
    }

    public void updateSelf() {
        sketchSprite.resetScaleCenter();
        sketchSprite.resetRotationCenter();
        updateBounds();
        if (lineLoop != null) lineLoop.detachSelf();
        this.lineLoop = new LineLoop(0, 0, 5, 100, ResourceManager.getInstance().vbom);
        lineLoop.setZIndex(2);
        lineLoop.setColor(0, 1, 0);
        creationScene.attachChild(lineLoop);
        creationScene.sortChildren();
        if (points != null)
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
        setUpdated(true);
    }

    public void dispose() {
        if (lineLoop != null) {
            lineLoop.dispose();
            lineLoop.detachSelf();
        }
    }

    public void resume() {
        // updateSelf();
    }

    public org.andengine.util.adt.color.Color getColorAt(float touchX, float touchY, int radius) {
        setPipeCircleRadius(radius);
        float[] pos = sketchSprite.convertSceneCoordinatesToLocalCoordinates(touchX, touchY);
        Bitmap bitmap = ResourceManager.getInstance().sketchBitmap;
        if (pos[0] < sketchSprite.getWidth()
                && pos[0] > 0
                && pos[1] < sketchSprite.getHeight()
                && pos[1] > 0) {
            pipeCircle.onUpdated(pos[0], pos[1]);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int x = (int) Math.floor(pos[0] / (2 * sketchSprite.getWidth() / 2) * width);
            int y = height - (int) Math.floor(pos[1] / (2 * sketchSprite.getHeight() / 2) * height);

            return MyColorUtils.getAverageRGBCircle(bitmap, x, y, 5);
        }
        return null;
    }

    public float getX() {
        return sketchSprite.getX();
    }

    public float getY() {
        return this.sketchSprite.getY();
    }


    public void updateWidth(float width) {
        this.sketchSprite.setWidth(width);
        imageShapeModel.setWidth(width);
    }

    public void updateHeight(float height) {
        this.sketchSprite.setHeight(height);
        imageShapeModel.setHeight(height);
    }

    public void updatePosition(float x, float y) {
        this.sketchSprite.setPosition(x, y);
        imageShapeModel.setX(x);
        imageShapeModel.setY(y);
    }

    public void updateRotation(float angle) {
        this.sketchSprite.setRotation(angle);
        imageShapeModel.setRotation(angle);
    }

    public void updateWidthWithRatio(float disX) {
        updateWidth(disX);
        updateHeight(disX * ratio);
    }

    public float getWidth(){
        return sketchSprite.getWidth();
    }
    public float getHeight(){
        return sketchSprite.getHeight();
    }
    public float getRotation() {
        return this.sketchSprite.getRotation();
    }
}
