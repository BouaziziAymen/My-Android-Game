package com.evolgames.mesh.mosaic;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.IMeshVertexBufferObject;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ModifiedMesh extends Mesh {


    public ModifiedMesh(float pX, float pY, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, 0, 0, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager);


    }


    public ModifiedMesh(float pX, float pY, float pWidth, float pHeight, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, pWidth, pHeight, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, DrawType.DYNAMIC);
    }

    public ModifiedMesh(float pX, float pY, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        this(pX, pY, 0, 0, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, pDrawType);
    }

    public ModifiedMesh(float pX, float pY, float pWidth, float pHeight, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        this(pX, pY, pWidth, pHeight, pVertexCount, pDrawMode, new MosaicMeshVertexBufferObject(pVertexBufferObjectManager, pBufferData, pBufferData.length/3, pDrawType, true, ModifiedMesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));


    }


    public ModifiedMesh(float pX, float pY, float pWidth, float pHeight, int pVertexCount, DrawMode pDrawMode, IMeshVertexBufferObject pMeshVertexBufferObject) {
        super(pX,  pY,  pWidth,  pHeight,  pVertexCount,  pDrawMode,  pMeshVertexBufferObject);


    }

}
