package com.evolgames.entities.particles.modifiers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.particles.emitters.RelativePolygonEmitter;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.transformation.Transformation;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BezierModifier implements IParticleModifier<UncoloredSprite> {
    private final RelativePolygonEmitter target;
    private final float lifespan;
    private final AtomicInteger atomicInteger = new AtomicInteger();
    private final float[] C1;
    private final float[] C2; // Points and control points
    private final HashMap<Integer, float[]> map;

    public BezierModifier(RelativePolygonEmitter target, float lifespan) {
        this.C1 = new float[]{0, 0};
        this.C2 = new float[]{0, 0};
        this.map = new HashMap<>();
        this.target = target;
        this.lifespan = lifespan;
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        final int id = (int) pParticle.getEntity().getUserData();
        final float[] data = map.get(id);
        if(data==null){
            return;
        }
        final float t = pParticle.getLifeTime() /lifespan;
        final float x = (float) (Math.pow(1 - t, 3) * data[0] + 3 * t * Math.pow(1 - t, 2) * data[5] + 3 * Math.pow(t, 2) * (1 - t) * data[7] + Math.pow(t, 3) * data[2]);
        final float y = (float) (Math.pow(1 - t, 3) * data[1] + 3 * t * Math.pow(1 - t, 2) * data[6] + 3 * Math.pow(t, 2) * (1 - t) * data[8] + Math.pow(t, 3) * data[3]);
        pParticle.getEntity().setPosition(x, y); // Set the position of the particle
    }


    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
        int id = atomicInteger.getAndIncrement();
        pParticle.getEntity().setUserData(id);
        float[] end = new float[2];
        this.target.getPositionOffset(end);

        this.map.put(id, new float[]{pParticle.getEntity().getX(), pParticle.getEntity().getY(), end[0], end[1], (float) (1-0.01f*Math.random()),C1[0],C1[1],C2[0],C2[1]});
    }


    public void transform(Vector2 center1, Vector2 center2) {
        C1[0] =center1.x;
        C1[1] =center1.y+50;
        C2[0] = center2.x;
        C2[1] = center2.y;
    }
}
