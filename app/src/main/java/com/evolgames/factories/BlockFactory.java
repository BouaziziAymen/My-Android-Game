package com.evolgames.factories;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.DecorationBlockConcrete;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blocks.CoatingBlock;

import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.properties.BlockCProperties;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.mesh.mosaic.MosaicMesh;
import com.evolgames.pools.BlockPool;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;
import java.util.ArrayList;
import java.util.Collections;

import static com.evolgames.helpers.utilities.GeometryUtils.transformation;

public class BlockFactory {

    public static BlockA createBlockA(ArrayList<Vector2> vertices, BlockAProperties properties, int ID) {
        return createBlockA(vertices, properties, ID, true, 0);
    }


    public static BlockA createBlockA(ArrayList<Vector2> vertices, BlockAProperties properties, int ID, int order) {
        return createBlockA(vertices, properties, ID, true, order);
    }


    public static BlockA createBlockA(ArrayList<Vector2> vertices, BlockAProperties properties, int ID, boolean firstTime, int order) {
        BlockA createdBlock = new BlockA();
        createdBlock.initialization(vertices,properties,ID,firstTime);
        createdBlock.setOrder(order);
        createdBlock.setLiquidQuantity((int) (createdBlock.getProperties().getJuicinessDensity()*createdBlock.getArea()*20));
        return createdBlock;
    }

    public static DecorationBlockConcrete createBlockB(ArrayList<Vector2> vertices, DecorationProperties properties, int ID) {
        DecorationBlockConcrete blockb = new DecorationBlockConcrete();
        blockb.initialization(vertices, properties, ID,true);
        return blockb;
    }
    public static DecorationBlockConcrete createBlockB(ArrayList<Vector2> vertices, DecorationProperties properties, int ID, ArrayList<Vector2> borders, Vector2 center) {
       DecorationBlockConcrete blockB = createBlockB(vertices,properties,ID);
        blockB.applyClip(borders);
        return blockB;
    }
    public static CoatingBlock createCoatingBlock(MosaicMesh mesh, ArrayList<Vector2> vertices, CoatingProperties properties, int layerId, int ID, ArrayList<Vector2> borders) {
        CoatingBlock coatingBlock = new CoatingBlock();
        coatingBlock.initialization(vertices,properties,ID,true);
        coatingBlock.applyClip(borders);
        coatingBlock.setMesh(mesh);
        coatingBlock.setLayerId(layerId);
        return coatingBlock;
    }

    public static StainBlock createBlockC(Vector2 localPosition, float angle, ArrayList<Vector2> clipPath, ITextureRegion textureRegion, Color color) {
        float halfWidth = textureRegion.getWidth() / 2f;
        float halfHeight = textureRegion.getHeight() / 2f;

        ArrayList<Vector2> imageBounds = new ArrayList<>();
        imageBounds.add(new Vector2(-halfWidth, -halfHeight));
        imageBounds.add(new Vector2(-halfWidth, halfHeight));
        imageBounds.add(new Vector2(halfWidth, halfHeight));
        imageBounds.add(new Vector2(halfWidth, -halfHeight));


        float[] verticesData = new float[]{-halfWidth, -halfHeight, -halfWidth, halfHeight, halfWidth, halfHeight, halfWidth, -halfHeight};
        transformation.setToIdentity();
        transformation.preTranslate(localPosition.x, localPosition.y);
        transformation.preRotate(angle);
        transformation.transform(verticesData);//vertices in real position

        for (int i = 0; i < imageBounds.size(); i++) {
            imageBounds.get(i).set(verticesData[2 * i], verticesData[2 * i + 1]);
        }

//initalization of stain Block
        if (!GeometryUtils.IsClockwise(imageBounds)) Collections.reverse(imageBounds);

        ArrayList<Vector2> clippedImageBounds = BlockUtils.applyClip(imageBounds, clipPath);
        if(clippedImageBounds==null)return null;

        StainBlock stainBlock = new StainBlock();
        BlockCProperties properties = new BlockCProperties(textureRegion, localPosition, angle,color);
        stainBlock.initialization(clippedImageBounds, properties, 0,true);

        return stainBlock;

    }

}
