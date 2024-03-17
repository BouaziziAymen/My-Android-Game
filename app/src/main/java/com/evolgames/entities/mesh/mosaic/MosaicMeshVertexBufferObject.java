package com.evolgames.entities.mesh.mosaic;

import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.HighPerformanceMeshVertexBufferObject;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.color.Color;

public class MosaicMeshVertexBufferObject extends HighPerformanceMeshVertexBufferObject {

    private final Color color = new Color(0, 0, 0);

    MosaicMeshVertexBufferObject(
            VertexBufferObjectManager pVertexBufferObjectManager,
            float[] pBufferData,
            int pVertexCount,
            DrawType pDrawType,
            boolean pAutoDispose,
            VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super(
                pVertexBufferObjectManager,
                pBufferData,
                pVertexCount,
                pDrawType,
                pAutoDispose,
                pVertexBufferObjectAttributes);
    }

    void updateColors(MosaicMesh mosaicMesh) {
        float[] bufferData = mBufferData;
        float packedColor;
        int offset = 0;
        int[] layersVertexCount = mosaicMesh.getLayersVertexCount();
        int length = layersVertexCount.length;
        for (int i = 0; i < length; i++) {
            if (mosaicMesh.getColors()[i] == null) {
                continue;
            }
            color.set(mosaicMesh.getColors()[i]);
            color.setAlpha(color.getAlpha() * mosaicMesh.getAlpha());
            packedColor = color.getABGRPackedFloat();

            for (int j = 0; j < layersVertexCount[i]; j++) {
                bufferData[(j + offset) * Mesh.VERTEX_SIZE + Mesh.COLOR_INDEX] = packedColor;
            }
            offset += layersVertexCount[i];
        }
        setDirtyOnHardware();
    }
}
