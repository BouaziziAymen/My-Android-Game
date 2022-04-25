package com.evolgames.mesh.batch;


import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;


import static com.evolgames.mesh.batch.TexturedMeshBatch.*;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:35:28 - 29.03.2012
 */
public class HighPerformanceTexturedMeshBatchVertexBufferObject extends HighPerformanceVertexBufferObject {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private int mBufferDataOffset;

    // ===========================================================
    // Constructors
    // ===========================================================

     HighPerformanceTexturedMeshBatchVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================


    public int getBufferDataOffset() {
        return this.mBufferDataOffset;
    }


     void setBufferDataOffset(final int pBufferDataOffset) {
        this.mBufferDataOffset = pBufferDataOffset;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    /**
     * 1-3
     * |X|
     * 2-4
     */

     void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pColorABGRPackedInt) {
        final float[] bufferData = this.getBufferData();
        final int bufferDataOffset = this.mBufferDataOffset;

        final float u = pTextureRegion.getU();
        final float v = pTextureRegion.getV();
        final float u2 = pTextureRegion.getU2();
        final float v2 = pTextureRegion.getV2();


        bufferData[bufferDataOffset + VERTEX_INDEX_X] = pX1;
        bufferData[bufferDataOffset + 0 * VERTEX_SIZE + VERTEX_INDEX_Y] = pY1;
        bufferData[bufferDataOffset + 0 * VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;
        bufferData[bufferDataOffset + 0 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = u;
        bufferData[bufferDataOffset + 0 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = v2;

        bufferData[bufferDataOffset + 1 * VERTEX_SIZE + VERTEX_INDEX_X] = pX2;
        bufferData[bufferDataOffset + 1 * VERTEX_SIZE + VERTEX_INDEX_Y] = pY2;
        bufferData[bufferDataOffset + 1 * VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;
        bufferData[bufferDataOffset + 1 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = u2;
        bufferData[bufferDataOffset + 1 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = v2;


        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_X] = pX3;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_Y] = pY3;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = u2;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = v;

        this.mBufferDataOffset += 15;//TexturedMeshBatch.SPRITE_SIZE;
    }


     void addWithPackedColor(final float[] meshData, final float pColorABGRPackedInt) {
        final float[] bufferData = this.getBufferData();
        int bufferDataOffset = this.mBufferDataOffset;

        for (int i = 0; i < meshData.length; i += 15) {
            float x1 = meshData[i];
            float y1 = meshData[i + 1];
            float u1 = meshData[i + 3];
            float v1 = meshData[i + 4];
            float x2 = meshData[i + 5];
            float y2 = meshData[i + 6];
            float u2 = meshData[i + 8];
            float v2 = meshData[i + 9];
            float x3 = meshData[i + 10];
            float y3 = meshData[i + 11];
            float u3 = meshData[i + 13];
            float v3 = meshData[i + 14];

            bufferData[bufferDataOffset + VERTEX_INDEX_X] = x1;
            bufferData[bufferDataOffset + VERTEX_INDEX_Y] = y1;
            bufferData[bufferDataOffset + COLOR_INDEX] = pColorABGRPackedInt;
            bufferData[bufferDataOffset + TEXTURECOORDINATES_INDEX_U] = u1;
            bufferData[bufferDataOffset + TEXTURECOORDINATES_INDEX_V] = v1;

            bufferData[bufferDataOffset +  VERTEX_SIZE + VERTEX_INDEX_X] = x2;
            bufferData[bufferDataOffset +  VERTEX_SIZE + VERTEX_INDEX_Y] = y2;
            bufferData[bufferDataOffset +  VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;
            bufferData[bufferDataOffset +  VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = u2;
            bufferData[bufferDataOffset +  VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = v2;


            bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_X] = x3;
            bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_Y] = y3;
            bufferData[bufferDataOffset + 2 * VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;
            bufferData[bufferDataOffset + 2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = u3;
            bufferData[bufferDataOffset + 2 * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = v3;

            bufferDataOffset += 15;

        }
        this.mBufferDataOffset = bufferDataOffset;

    }


// ===========================================================
// Methods
// ===========================================================

// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
}