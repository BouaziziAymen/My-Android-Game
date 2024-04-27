package com.evolgames.dollmutilationgame.utilities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vector2Utils {
    private static final Random random = new Random();
    public static List<Vector2> generateRandomPointsInsidePolygon(int n, Vector2  localImpactPoint, LayerBlock layerBlock, GameEntity gameEntity) {
        Vector2 u1 = new Vector2(1,0);
        Vector2 coatingCenter = layerBlock.getBlockGrid().getNearestCoatingBlockSimple(localImpactPoint).getPosition();
        Vector2 u = coatingCenter.cpy().sub(localImpactPoint).nor();
        Vector2 localCenter = localImpactPoint.cpy().add(u);

        final int steps =50;
        final float dA = (float) (2*Math.PI/steps);
        Vector2 center = gameEntity.getBody().getWorldPoint(localCenter.cpy().mul(1/32f)).cpy();
        List<Vector2> list = new ArrayList<>();
        for(int i=0;i<steps;i++){
            GeometryUtils.rotateVectorRad(u1,dA);
            Vector2 far = localCenter.cpy().add(u1.x*200f,u1.y*200f);
            Vector2 worldFar = gameEntity.getBody().getWorldPoint(far.cpy().mul(1/32f)).cpy();
            Vector2 p = gameEntity.getScene().getWorldFacade().detectFirstIntersectionPointWithLayerBlock(worldFar,center,layerBlock);
            if(p!=null) {
                Vector2 lp = gameEntity.getBody().getLocalPoint(p).cpy().mul(32f);
                list.add(lp);
                //    GameScene.plotter2.drawLineOnEntity(nearest, lp, Color.RED, 1f, gameEntity.getMesh());
            }
        }
        float totalLength = 0;
        for (Vector2 p : list) {
            totalLength += p.dst(localCenter);
        }
        List<Vector2> result = new ArrayList<>();
        for(int i=0;i<n;i++) {
            float randomValue = random.nextFloat() * totalLength;
            float cumulativeLength = 0;
            Vector2 chosen = null;
            for (Vector2 p : list) {
                cumulativeLength += p.dst(localCenter);
                if (randomValue < cumulativeLength) {
                    chosen = p;
                    break;
                }
            }
            if(chosen!=null) {
                result.add(pickRandomPoint(localCenter, chosen));
            }
        }
        return result;
    }
    static public Vector2 pickRandomPoint(Vector2 start, Vector2 end) {
        float normalizedDistance = (float) (start.dst(end)/4f * Math.abs(random.nextGaussian()));
        Vector2 u = end.cpy().sub(start).nor();
        float x = start.x + normalizedDistance* u.x;
        float y = start.y + normalizedDistance * u.y;
        return new Vector2(x,y);
    }
}
