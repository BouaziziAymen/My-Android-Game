package is.kul.learningandengine.particlesystems.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class ExpireParticleInitializer<T extends IEntity> implements IParticleInitializer<T> {
	// ===========================================================
	// PhysicsConstants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMinLifeTime;
	private float mMaxLifeTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExpireParticleInitializer(float pLifeTime) {
		this(pLifeTime, pLifeTime);
	}

	public ExpireParticleInitializer(float pMinLifeTime, float pMaxLifeTime) {
        mMinLifeTime = pMinLifeTime;
        mMaxLifeTime = pMaxLifeTime;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinLifeTime() {
		return mMinLifeTime;
	}

	public float getMaxLifeTime() {
		return mMaxLifeTime;
	}

	public void setLifeTime(float pLifeTime) {
        mMinLifeTime = pLifeTime;
        mMaxLifeTime = pLifeTime;
	}

	public void setLifeTime(float pMinLifeTime, float pMaxLifeTime) {
        mMinLifeTime = pMinLifeTime;
        mMaxLifeTime = pMaxLifeTime;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(Particle<T> pParticle) {
		pParticle.setExpireTime(MathUtils.RANDOM.nextFloat() * (mMaxLifeTime - mMinLifeTime) + mMinLifeTime);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
