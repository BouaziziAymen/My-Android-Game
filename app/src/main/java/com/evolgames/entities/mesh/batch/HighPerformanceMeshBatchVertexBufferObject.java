package com.evolgames.entities.mesh.batch;

import static com.evolgames.entities.mesh.batch.MeshBatch.COLOR_INDEX;
import static com.evolgames.entities.mesh.batch.MeshBatch.VERTEX_INDEX_X;
import static com.evolgames.entities.mesh.batch.MeshBatch.VERTEX_INDEX_Y;
import static com.evolgames.entities.mesh.batch.MeshBatch.VERTEX_SIZE;

import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:35:28 - 29.03.2012
 */
public class HighPerformanceMeshBatchVertexBufferObject extends HighPerformanceVertexBufferObject {
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

    public HighPerformanceMeshBatchVertexBufferObject(
            final VertexBufferObjectManager pVertexBufferObjectManager,
            final int pCapacity,
            final DrawType pDrawType,
            final boolean pAutoDispose,
            final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super(
                pVertexBufferObjectManager,
                pCapacity,
                pDrawType,
                pAutoDispose,
                pVertexBufferObjectAttributes);
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
     * 1-3 |X| 2-4
     */
    void addWithPackedColor(
            final float pX1,
            final float pY1,
            final float pX2,
            final float pY2,
            final float pX3,
            final float pY3,
            final float pColorABGRPackedInt) {
        final float[] bufferData = this.getBufferData();
        final int bufferDataOffset = this.mBufferDataOffset;

        bufferData[bufferDataOffset + VERTEX_INDEX_X] = pX1;
        bufferData[bufferDataOffset + VERTEX_INDEX_Y] = pY1;
        bufferData[bufferDataOffset + COLOR_INDEX] = pColorABGRPackedInt;

        bufferData[bufferDataOffset + VERTEX_SIZE + VERTEX_INDEX_X] = pX2;
        bufferData[bufferDataOffset + VERTEX_SIZE + VERTEX_INDEX_Y] = pY2;
        bufferData[bufferDataOffset + VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;

        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_X] = pX3;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + VERTEX_INDEX_Y] = pY3;
        bufferData[bufferDataOffset + 2 * VERTEX_SIZE + COLOR_INDEX] = pColorABGRPackedInt;

        this.mBufferDataOffset += 9; // TexturedMeshBatch.SPRITE_SIZE;
    }

    void addWithPackedColor(final float[] meshData, final float pColorABGRPackedInt) {
        final float[] bufferData = this.getBufferData();
        int bufferDataOffset = this.mBufferDataOffset;
        for (int i = 0; i < meshData.length; i += 9) {
            float x1 = meshData[i];
            float y1 = meshData[i + 1];
            float x2 = meshData[i + 3];
            float y2 = meshData[i + 4];
            float x3 = meshData[i + 6];
            float y3 = meshData[i + 7];

            bufferData[bufferDataOffset + VERTEX_INDEX_X] = x1;
            bufferData[bufferDataOffset + VERTEX_INDEX_Y] = y1;
            bufferData[bufferDataOffset + COLOR_INDEX] = pColorABGRPackedInt;

            bufferData[bufferDataOffset + 3 + VERTEX_INDEX_X] = x2;
            bufferData[bufferDataOffset + 3 + VERTEX_INDEX_Y] = y2;
            bufferData[bufferDataOffset + 3 + COLOR_INDEX] = pColorABGRPackedInt;

            bufferData[bufferDataOffset + 2 * 3 + VERTEX_INDEX_X] = x3;
            bufferData[bufferDataOffset + 2 * 3 + VERTEX_INDEX_Y] = y3;
            bufferData[bufferDataOffset + 2 * 3 + COLOR_INDEX] = pColorABGRPackedInt;

            bufferDataOffset += 9;
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
