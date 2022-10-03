package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseStrongInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseStrongInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseStrongInOut() {

	}

	public static EaseStrongInOut getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseStrongInOut();
		}
		return INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getPercentage(final float pSecondsElapsed, final float pDuration) {
		final float percentage = pSecondsElapsed / pDuration;

		if (percentage < 0.5f) {
			return EaseStrongIn.getValue(2 * percentage);
		} else if (percentage < 1f) {
			return 0.5f * EaseStrongOut.getValue(percentage * 2);
		}
		return 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
