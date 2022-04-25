package com.evolgames.mesh.batch;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:35:06 - 29.03.2012
 */
public interface IMeshBatchVertexBufferObject extends IVertexBufferObject {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================


    public void setBufferDataOffset(final int pBufferDataOffset);


    void addWithPackedColor(ITextureRegion pTextureRegion, float x1, float y1, float x2, float y2, float x3, float y3, float color);

    void addWithPackedColor(float[] meshData, float packedColor);
}