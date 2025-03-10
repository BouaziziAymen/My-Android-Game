package com.evolgames.dollmutilationgame.userinterface.view.inputs.controllers;

import org.andengine.engine.camera.Camera;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 00:21:55 - 11.07.2010
 */
public class AnalogController extends Controller implements IClickDetectorListener {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final ClickDetector mClickDetector = new ClickDetector(this);
    private boolean touchEnabled;

    public AnalogController(
            final float pX,
            final float pY,
            final Camera pCamera,
            final float pTimeBetweenUpdates,
            final AnalogController.IAnalogOnScreenControlListener pAnalogOnScreenControlListener) {
        super(pX, pY, pCamera, pTimeBetweenUpdates, pAnalogOnScreenControlListener);

        this.mClickDetector.setEnabled(false);
    }

    // ===========================================================
    // Constructors
    // ===========================================================

    public AnalogController(
            final float pX,
            final float pY,
            final Camera pCamera,
            final float pTimeBetweenUpdates,
            final long pOnControlClickMaximumMilliseconds,
            AnalogController.IAnalogOnScreenControlListener pAnalogOnScreenControlListener) {
        super(pX, pY, pCamera, pTimeBetweenUpdates, pAnalogOnScreenControlListener);

        this.mClickDetector.setTriggerClickMaximumMilliseconds(pOnControlClickMaximumMilliseconds);
    }

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public AnalogController.IAnalogOnScreenControlListener getOnScreenControlListener() {
        return (AnalogController.IAnalogOnScreenControlListener) super.getOnScreenControlListener();
    }

    public void setOnControlClickEnabled(final boolean pOnControlClickEnabled) {
        this.mClickDetector.setEnabled(pOnControlClickEnabled);
    }

    public void setOnControlClickMaximumMilliseconds(final long pOnControlClickMaximumMilliseconds) {
        this.mClickDetector.setTriggerClickMaximumMilliseconds(pOnControlClickMaximumMilliseconds);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onClick(
            final ClickDetector pClickDetector,
            final int pPointerID,
            final float pSceneX,
            final float pSceneY) {
        this.getOnScreenControlListener().onControlClick(this);
    }

    @Override
    protected boolean onHandleControlBaseTouched(
            final TouchEvent pSceneTouchEvent,
            final float pTouchAreaLocalX,
            final float pTouchAreaLocalY) {
        if (!touchEnabled) return false;
        if (pSceneTouchEvent.isActionUp()) getOnScreenControlListener().onControlReleased(this);
        this.mClickDetector.onSceneTouchEvent(null, pSceneTouchEvent);
        return super.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
        if (pRelativeX * pRelativeX + pRelativeY * pRelativeY <= 0.25f) {
            super.onUpdateControlKnob(pRelativeX, pRelativeY);
        } else {
            final float angleRad = MathUtils.atan2(pRelativeY, pRelativeX);
            super.onUpdateControlKnob(
                    (float) Math.cos(angleRad) * 0.5f, (float) Math.sin(angleRad) * 0.5f);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface IAnalogOnScreenControlListener extends IOnScreenControlListener {
        // ===========================================================
        // PhysicsConstants
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        void onControlClick(final AnalogController pAnalogOnScreenControl);

        void onControlReleased(AnalogController pAnalogOnScreenControl);
    }
}
