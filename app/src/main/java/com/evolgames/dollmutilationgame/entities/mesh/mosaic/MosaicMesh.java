package com.evolgames.dollmutilationgame.entities.mesh.mosaic;

import com.evolgames.dollmutilationgame.activity.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;
import org.andengine.util.algorithm.collision.EntityCollisionChecker;

public class MosaicMesh extends ModifiedMesh {
    private final Color[] colors;
    private final int[] layersVertexCount;
    private final Color singleColor;
    private float[] bounds;

    public MosaicMesh(
            float x, float y, float rot, float[] data, Color[] colors, int[] layersVertexCount) {
        super(x, y, data, data.length / 3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        this.layersVertexCount = layersVertexCount;
        this.colors = colors;
        this.singleColor = null;
        setRotation((float) Math.toDegrees(rot));
        onUpdateColor();
    }

    public MosaicMesh(
            float x, float y, float rot, float[] data, Color singleColor, int[] layersVertexCount) {
        super(x, y, data, data.length / 3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        this.layersVertexCount = layersVertexCount;
        this.singleColor = singleColor;
        this.colors = null;
        setRotation((float) Math.toDegrees(rot));
        onUpdateColor();
    }

    public Color getSingleColor() {
        return singleColor;
    }

    @Override
    protected void draw(GLState pGLState, Camera pCamera) {
        int[] layersVertexCount = getLayersVertexCount();
        int offset = 0;
        for (int layerVertexCount : layersVertexCount) {
            this.mMeshVertexBufferObject.draw(this.mDrawMode, offset, layerVertexCount);
            offset += layerVertexCount;
        }
    }

    @Override
    protected void onUpdateColor() {
        ((MosaicMeshVertexBufferObject) this.mMeshVertexBufferObject).updateColors(this);
    }

    @Override
    public void setAlpha(float pAlpha) {
        super.setAlpha(pAlpha);
    }

    public void onColorsUpdated() {
        onUpdateColor();
    }

    public Color[] getColors() {
        return colors;
    }

    public int[] getLayersVertexCount() {
        return layersVertexCount;
    }


    @Override
    public boolean isCulled(final Camera pCamera) {
        return !EntityCollisionChecker.isVisible(pCamera,bounds, getLocalToSceneTransformation());
    }

    public void setBounds(float[] bounds) {
        this.bounds = bounds;
    }

    public float[] getBounds() {
        return bounds;
    }
}
