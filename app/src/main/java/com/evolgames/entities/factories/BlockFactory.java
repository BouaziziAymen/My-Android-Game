package com.evolgames.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.DecorationBlock;
import com.evolgames.entities.blocks.StainBlock;

import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.properties.StainProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.physics.PhysicsConstants;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;
import java.util.ArrayList;
import java.util.Collections;

import static com.evolgames.helpers.utilities.GeometryUtils.transformation;

public class BlockFactory {

    public static LayerBlock createLayerBlock(ArrayList<Vector2> vertices, LayerProperties properties, int ID) {
        return BlockFactory.createLayerBlock(vertices, properties, ID, 0, true);
    }


    public static LayerBlock createLayerBlock(ArrayList<Vector2> vertices, LayerProperties properties, int ID, int order) {
        return BlockFactory.createLayerBlock(vertices, properties, ID, order, true);
    }


    public static LayerBlock createLayerBlock(ArrayList<Vector2> vertices, LayerProperties properties, int ID, int order, boolean fillGrid) {
        LayerBlock createdBlock = new LayerBlock();
        createdBlock.initialization(vertices,properties,ID,fillGrid);
        createdBlock.setOrder(order);
        createdBlock.setLiquidQuantity((int) (createdBlock.getProperties().getJuicinessDensity()*createdBlock.getBlockArea()* PhysicsConstants.LIQUID_DENSITY_CONSTANT));
        return createdBlock;
    }

    public static DecorationBlock createDecorationBlock(ArrayList<Vector2> vertices, DecorationProperties properties, int ID) {
        DecorationBlock decorationBlock = new DecorationBlock();
        decorationBlock.initialization(vertices, properties, ID);
        return decorationBlock;
    }
    public static DecorationBlock createDecorationBlock(ArrayList<Vector2> vertices, DecorationProperties properties, int ID, ArrayList<Vector2> borders, Vector2 center) {
       DecorationBlock decorationBlock = createDecorationBlock(vertices,properties,ID);
        decorationBlock.applyClip(borders);
        return decorationBlock;
    }

    public static StainBlock createStainBlock(Vector2 localPosition, float angle, ArrayList<Vector2> clipPath, ITextureRegion textureRegion, Color color) {
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

        if (!GeometryUtils.IsClockwise(imageBounds)) Collections.reverse(imageBounds);

        ArrayList<Vector2> clippedImageBounds = BlockUtils.applyClip(imageBounds, clipPath);
        if(clippedImageBounds==null){
            return null;
        }

        StainBlock stainBlock = new StainBlock();
        StainProperties properties = new StainProperties(textureRegion, localPosition, angle,color);
        stainBlock.initialization(clippedImageBounds, properties, 0);

        return stainBlock;

    }

}
