package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.BaseSingleValueSpanParticleModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AlphaParticleModifier<T extends IEntity>
        extends BaseSingleValueSpanParticleModifier<T> {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public AlphaParticleModifier(float pFromTime, float pToTime, float pFromAlpha, float pToAlpha) {
        this(pFromTime, pToTime, pFromAlpha, pToAlpha, EaseLinear.getInstance());
    }

    public AlphaParticleModifier(
            float pFromTime,
            float pToTime,
            float pFromAlpha,
            float pToAlpha,
            IEaseFunction pEaseFunction) {
        super(pFromTime, pToTime, pFromAlpha, pToAlpha, pEaseFunction);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onSetInitialValue(Particle<T> pParticle, float pAlpha) {
        pParticle.getEntity().setAlpha(pAlpha);
    }

    @Override
    protected void onSetValue(Particle<T> pParticle, float pPercentageDone, float pAlpha) {
        pParticle.getEntity().setAlpha(pAlpha);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
