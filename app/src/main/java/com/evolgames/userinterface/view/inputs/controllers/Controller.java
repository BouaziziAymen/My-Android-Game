package com.evolgames.userinterface.view.inputs.controllers;


import android.view.MotionEvent;

import com.evolgames.gameengine.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;



public abstract class Controller extends HUD implements IOnSceneTouchListener {

    private static final int INVALID_POINTER_ID = -1;

    private final Sprite mControlBase;
    private float mControlValueX;
    private float mControlValueY;

    private final Controller.IOnScreenControlListener mOnScreenControlListener;

    private int mActivePointerID = Controller.INVALID_POINTER_ID;

    // ===========================================================
    // Constructors
    // ===========================================================

    public Controller(final float pX, final float pY, final Camera pCamera,final float pTimeBetweenUpdates, final Controller.IOnScreenControlListener pOnScreenControlListener) {
        this.setCamera(pCamera);

        this.mOnScreenControlListener = pOnScreenControlListener;
        /* Create the control base. */
        this.mControlBase = new Sprite(pX, pY, ResourceManager.getInstance().controlButton, ResourceManager.getInstance().vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                return Controller.this.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };

        /* Create the control knob. */
        this.onHandleControlKnobReleased();

        /* Register listeners and add objects to this HUD. */
        this.setOnSceneTouchListener(this);
        this.registerTouchArea(this.mControlBase);
        this.registerUpdateHandler(new TimerHandler(pTimeBetweenUpdates, true, pTimerHandler -> Controller.this.mOnScreenControlListener.onControlChange(Controller.this, Controller.this.mControlValueX, Controller.this.mControlValueY)));

        this.attachChild(this.mControlBase);

        this.setTouchAreaBindingOnActionDownEnabled(true);
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {

        final int pointerID = pSceneTouchEvent.getPointerID();
        if (pointerID == this.mActivePointerID) {
            this.onHandleControlBaseLeft();

            switch (pSceneTouchEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.mActivePointerID = Controller.INVALID_POINTER_ID;
            }
        }
        return false;
    }

    /**
     * When the touch happened outside of the bounds of this OnScreenControl.
     */
    protected void onHandleControlBaseLeft() {
        this.onUpdateControlKnob(0, 0);
    }

    /**
     * When the OnScreenControl was released.
     */
    protected void onHandleControlKnobReleased() {
        this.onUpdateControlKnob(0, 0);
    }

    protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        final int pointerID = pSceneTouchEvent.getPointerID();

        switch (pSceneTouchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.mActivePointerID == Controller.INVALID_POINTER_ID) {
                    this.mActivePointerID = pointerID;
                    this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mActivePointerID == pointerID) {
                    this.mActivePointerID = Controller.INVALID_POINTER_ID;
                    this.onHandleControlKnobReleased();
                    return true;
                }
                break;
            default:
                if (this.mActivePointerID == pointerID) {
                    this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
                    return true;
                }
                break;
        }
        return true;
    }

    private void updateControlKnob(final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        final Sprite controlBase = this.mControlBase;

        final float relativeX = (MathUtils.bringToBounds(0, controlBase.getWidth(), pTouchAreaLocalX) / controlBase.getWidth()) - 0.5f;
        final float relativeY = (MathUtils.bringToBounds(0, controlBase.getHeight(), pTouchAreaLocalY) / controlBase.getHeight()) - 0.5f;

        this.onUpdateControlKnob(relativeX, relativeY);
    }

    public IOnScreenControlListener getOnScreenControlListener() {
        return mOnScreenControlListener;
    }

    public Sprite getControlBase() {
        return this.mControlBase;
    }
    /**
     * @param pRelativeX from <code>-0.5f</code> (left) to <code>0.5</code> (right).
     * @param pRelativeY from <code>-0.5f</code> (bottom) to <code>0.5f</code> (top).
     */
    protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
        this.mControlValueX = 2 * pRelativeX;
        this.mControlValueY = 2 * pRelativeY;
    }

    public enum Type{
        XYD(0),XY(1),X(2),Y(3);
        Type(int n){
            number = n;
        }
        final int number;
    }

    public interface IOnScreenControlListener {
        void onControlChange(final Controller pBaseOnScreenControl, final float pValueX, final float pValueY);
    }

    @Override
    public void setPosition(float pX, float pY) {
        getControlBase().setPosition(pX, pY);
    }
}
