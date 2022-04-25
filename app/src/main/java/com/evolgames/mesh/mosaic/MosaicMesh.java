package com.evolgames.mesh.mosaic;

import android.util.Log;

import com.evolgames.gameengine.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

public class MosaicMesh extends ModifiedMesh {
    private Color[] colors;
    private int[] layersVertexCount;
    private boolean[] visibilityArray;

    public MosaicMesh(float x, float y, float rot, float[] data, Color[] colors, int[] layersVertexCount) {
        super(x,y, data, data.length/3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        this.layersVertexCount = layersVertexCount;
        this.colors = colors;
        setRotation((float) Math.toDegrees(rot));
        onUpdateColor();
    }

    @Override
    protected void draw(GLState pGLState, Camera pCamera) {
        int[] layersVertexCount = getLayersVertexCount();
        //boolean[] visibilityArray = getVisibilityArray();
        int offset = 0;
        for(int i=0;i<layersVertexCount.length;i++) {

            int layerVertexCount = layersVertexCount[i];
            //if(visibilityArray[i])
            this.mMeshVertexBufferObject.draw(this.mDrawMode, offset, layerVertexCount);
            offset+=layerVertexCount;
        }
    }

    @Override
    protected void onUpdateColor() {
        ((MosaicMeshVertexBufferObject)this.mMeshVertexBufferObject).updateColors(this);
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


    public boolean[] getVisibilityArray() {
        return visibilityArray;
    }
}
