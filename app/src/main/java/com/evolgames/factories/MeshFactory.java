package com.evolgames.factories;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.mesh.mosaic.MosaicMesh;
import com.evolgames.scenes.GameScene;

import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

import static com.evolgames.helpers.utilities.GeometryUtils.transformation;


public class MeshFactory {

    private static EarClippingTriangulator triangulator = new EarClippingTriangulator();
    private static final MeshFactory INSTANCE = new MeshFactory();
    private GameScene gameScene;

    public static MeshFactory getInstance() {
        return INSTANCE;
    }





public MosaicMesh createMosaicMesh(float x, float y, float rot, ArrayList<BlockA> blocks){
    float[] data = BlockUtils.computeData(blocks);
    Color[] colors = BlockUtils.computeColors(blocks);
    int[] counts = BlockUtils.computeVertexCount(blocks);
    return new MosaicMesh(x, y, rot,data,colors,counts);
}


    public float[] computeData(List<Vector2> triangles){
        float[] meshData = new float[3*triangles.size()];
        for (int i = 0; i < triangles.size(); i++) {
            meshData[i * 3] = triangles.get(i).x;
            meshData[i * 3 + 1] = triangles.get(i).y;
            meshData[i * 3 + 2] = 0.0f;
        }
        return meshData;
    }






    public float[] getStainData(StainBlock stainBlock){
    List<Vector2> triangles = stainBlock.getTriangles();
    float[] data = new float[triangles.size()*5];
    float[] trianglesVertices = new float[triangles.size()*2];
    for(int i=0;i<triangles.size();i++){
        Vector2 vector = triangles.get(i);
        trianglesVertices[2*i]     = vector.x;
        trianglesVertices[2*i + 1] = vector.y;
        data[5*i] = vector.x;
        data[5*i+1] = vector.y;
    }
float localCenterX = stainBlock.getLocalCenterX();
float localCenterY = stainBlock.getLocalCenterY();
float angle = stainBlock.getLocalRotation();
ITextureRegion textureRegion = stainBlock.getTextureRegion();
    //transform inversed to get u and v
    transformation.setToIdentity();
    transformation.preTranslate(-localCenterX,-localCenterY);
    transformation.postRotate(-angle);
    transformation.transform(trianglesVertices);

    for(int i=0;i<triangles.size();i++) {
        float x = trianglesVertices[2*i];
        float y = trianglesVertices[2*i+1];
        float regionWidth = textureRegion.getWidth();
        float regionHeight = textureRegion.getHeight();
        float textureWidth = textureRegion.getTexture().getWidth();
        float textureHeight = textureRegion.getTexture().getHeight();
        float x0 = 0; // pMesh.getX0();
        float y0 = 0; //pMesh.getY0();
        float widthRatio = regionWidth / textureWidth;
        float heightRatio = regionHeight / textureHeight;
        float u = (x - x0) / regionWidth + 0.5f;
        float v = (y - y0) / -regionHeight + 0.5f;
        float U = u * widthRatio + textureRegion.getU();
        float V = v * heightRatio + textureRegion.getV();
        data[5*i+3] = U;
        data[5*i+4] = V;
        //stain block vertices contains real local x, y
    }

    return data;
}

    public void create(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public ArrayList<Vector2> triangulate(ArrayList<Vector2> vertices) {
        return (ArrayList<Vector2>) triangulator.computeTriangles(vertices);
    }
}
