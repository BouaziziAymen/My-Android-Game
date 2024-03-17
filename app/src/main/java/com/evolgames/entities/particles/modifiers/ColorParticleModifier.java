package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.BaseTripleValueSpanParticleModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:22:26 - 29.06.2010
 */
public class ColorParticleModifier<T extends IEntity>
        extends BaseTripleValueSpanParticleModifier<T> {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ColorParticleModifier(
            float pFromTime,
            float pToTime,
            float pFromRed,
            float pToRed,
            float pFromGreen,
            float pToGreen,
            float pFromBlue,
            float pToBlue) {
        this(
                pFromTime,
                pToTime,
                pFromRed,
                pToRed,
                pFromGreen,
                pToGreen,
                pFromBlue,
                pToBlue,
                EaseLinear.getInstance());
    }

    public ColorParticleModifier(
            float pFromTime,
            float pToTime,
            float pFromRed,
            float pToRed,
            float pFromGreen,
            float pToGreen,
            float pFromBlue,
            float pToBlue,
            IEaseFunction pEaseFunction) {
        super(
                pFromTime,
                pToTime,
                pFromRed,
                pToRed,
                pFromGreen,
                pToGreen,
                pFromBlue,
                pToBlue,
                pEaseFunction);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onSetInitialValues(Particle<T> pParticle, float pRed, float pGreen, float pBlue) {
        pParticle.getEntity().setColor(pRed, pGreen, pBlue);
    }

    @Override
    protected void onSetValues(
            Particle<T> pParticle, float pPercentageDone, float pRed, float pGreen, float pBlue) {
        pParticle.getEntity().setColor(pRed, pGreen, pBlue);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
