package com.evolgames.dollmutilationgame.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.blocks.DecorationBlock;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.blocks.StainBlock;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.utilities.BlockUtils;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.entities.properties.DecorationProperties;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.dollmutilationgame.entities.properties.StainProperties;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockFactory {

    public static LayerBlock createLayerBlock(
            List<Vector2> vertices, LayerProperties properties, int ID) {
        return BlockFactory.createLayerBlock(vertices, properties, ID, 0, true);
    }

    public static LayerBlock createLayerBlock(
            List<Vector2> vertices, LayerProperties properties, int ID, int order) {
        return BlockFactory.createLayerBlock(vertices, properties, ID, order, properties.getMaterialNumber()!=MaterialFactory.VOID);
    }

    public static LayerBlock createLayerBlock(
            List<Vector2> vertices, LayerProperties properties, int ID, int order, boolean fillGrid) {
        LayerBlock createdBlock = new LayerBlock();
        createdBlock.initialization(vertices, properties, ID, fillGrid);
        createdBlock.setOrder(order);
        createdBlock.setLiquidQuantity(
                (int)
                        (createdBlock.getProperties().getJuicinessDensity()
                                * createdBlock.getBlockArea()
                                * PhysicsConstants.LIQUID_DENSITY_CONSTANT));
        return createdBlock;
    }

    public static DecorationBlock createDecorationBlock(
            ArrayList<Vector2> vertices, DecorationProperties properties, int ID) {
        DecorationBlock decorationBlock = new DecorationBlock();
        decorationBlock.initialization(vertices, properties, ID);
        return decorationBlock;
    }

    public static StainBlock createStainBlock(
            Vector2 localPosition,
            float angle,
            List<Vector2> clipPath,
            int textureRegionIndex,
            Color color, float flammability) {
        ITextureRegion textureRegion =
                ResourceManager.getInstance().stainTextureRegions.getTextureRegion(textureRegionIndex);
        float halfWidth = textureRegion.getWidth() / 2f;
        float halfHeight = textureRegion.getHeight() / 2f;

        ArrayList<Vector2> imageBounds = new ArrayList<>();
        imageBounds.add(new Vector2(-halfWidth, -halfHeight));
        imageBounds.add(new Vector2(-halfWidth, halfHeight));
        imageBounds.add(new Vector2(halfWidth, halfHeight));
        imageBounds.add(new Vector2(halfWidth, -halfHeight));

        float[] verticesData =
                new float[]{
                        -halfWidth,
                        -halfHeight,
                        -halfWidth,
                        halfHeight,
                        halfWidth,
                        halfHeight,
                        halfWidth,
                        -halfHeight
                };
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(localPosition.x, localPosition.y);
        GeometryUtils.transformation.preRotate(angle);
        GeometryUtils.transformation.transform(verticesData); // vertices in real position

        for (int i = 0; i < imageBounds.size(); i++) {
            imageBounds.get(i).set(verticesData[2 * i], verticesData[2 * i + 1]);
        }

        if (!GeometryUtils.IsClockwise(imageBounds)) {
            Collections.reverse(imageBounds);
        }

        List<Vector2> clippedImageBounds = BlockUtils.applyClip(imageBounds, clipPath);
        if (clippedImageBounds == null) {
            return null;
        }

        StainBlock stainBlock = new StainBlock();
        StainProperties properties =
                new StainProperties(textureRegionIndex, localPosition, angle, color, flammability);
        stainBlock.initialization(clippedImageBounds, properties, 0);

        return stainBlock;
    }
}
