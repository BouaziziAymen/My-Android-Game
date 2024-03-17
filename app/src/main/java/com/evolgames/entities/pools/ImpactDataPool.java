package com.evolgames.entities.pools;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blockvisitors.ImpactData;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.pool.GenericPool;

public class ImpactDataPool {

    private static final GenericPool<ImpactData> POOL =
            new GenericPool<ImpactData>() {
                @Override
                protected ImpactData onAllocatePoolItem() {
                    return new ImpactData();
                }
            };

    public static ImpactData obtain(
            GameEntity gameEntity, LayerBlock impactedBlock, Vector2 worldPoint) {
        Vector2 localImpactPoint = Vector2Pool.obtain(gameEntity.getBody().getLocalPoint(worldPoint));
        ImpactData impactData = POOL.obtainPoolItem();
        impactData.setGameEntity(gameEntity);
        impactData.setImpactedBlock(impactedBlock);
        impactData.setWorldPoint(worldPoint);
        impactData.setLocalImpactPoint(localImpactPoint);
        return impactData;
    }

    public static ImpactData obtain(
            GameEntity gameEntity, LayerBlock impactedBlock, Vector2 worldPoint, float impactEnergy) {
        ImpactData impactData = ImpactDataPool.obtain(gameEntity, impactedBlock, worldPoint);
        impactData.setImpactImpulse(impactEnergy);
        return impactData;
    }

    public static void recycle(final ImpactData impactData) {
        Vector2Pool.recycle(impactData.getLocalImpactPoint());
        Vector2Pool.recycle(impactData.getWorldPoint());
        POOL.recyclePoolItem(impactData);
    }
}
