package is.kul.learningandengine.controls;


import android.view.MotionEvent;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import is.kul.learningandengine.ResourceManager;

public abstract class MyBaseOnScreenControl extends HUD implements IOnSceneTouchListener {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    private static final int INVALID_POINTER_ID = -1;

    // ===========================================================
    // Fields
    // ===========================================================

    private final TiledSprite mControlBase;
    private final Sprite mControlKnob;

    private float mControlValueX;
    private float mControlValueY;

    private final MyBaseOnScreenControl.IOnScreenControlListener mOnScreenControlListener;

    private int mActivePointerID = MyBaseOnScreenControl.INVALID_POINTER_ID;

    // ===========================================================
    // Constructors
    // ===========================================================

    public MyBaseOnScreenControl(final float pX, final float pY, final Camera pCamera,final float pTimeBetweenUpdates, final MyBaseOnScreenControl.IOnScreenControlListener pOnScreenControlListener) {
        this.setCamera(pCamera);

        this.mOnScreenControlListener = pOnScreenControlListener;
        /* Create the control base. */
        this.mControlBase = new TiledSprite(pX, pY, ResourceManager.getInstance().controlButton, ResourceManager.getInstance().vbom) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                return MyBaseOnScreenControl.this.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };

        /* Create the control knob. */
        this.mControlKnob = new Sprite(0, 0, ResourceManager.getInstance().mOnScreenControlKnobTextureRegion, ResourceManager.getInstance().vbom);
        this.onHandleControlKnobReleased();

        /* Register listeners and add objects to this HUD. */
        this.setOnSceneTouchListener(this);
        this.registerTouchArea(this.mControlBase);
        this.registerUpdateHandler(new TimerHandler(pTimeBetweenUpdates, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                MyBaseOnScreenControl.this.mOnScreenControlListener.onControlChange(MyBaseOnScreenControl.this, MyBaseOnScreenControl.this.mControlValueX, MyBaseOnScreenControl.this.mControlValueY);
            }
        }));

        this.attachChild(this.mControlBase);
        this.mControlBase.attachChild(this.mControlKnob);

        this.setTouchAreaBindingOnActionDownEnabled(true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public TiledSprite getControlBase() {
        return this.mControlBase;
    }

    public Sprite getControlKnob() {
        return this.mControlKnob;
    }

    public MyBaseOnScreenControl.IOnScreenControlListener getOnScreenControlListener() {
        return this.mOnScreenControlListener;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        final int pointerID = pSceneTouchEvent.getPointerID();
        if (pointerID == this.mActivePointerID) {
            this.onHandleControlBaseLeft();

            switch (pSceneTouchEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.mActivePointerID = MyBaseOnScreenControl.INVALID_POINTER_ID;
            }
        }
        return false;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void refreshControlKnobPosition() {
        this.onUpdateControlKnob(this.mControlValueX * 0.5f, this.mControlValueY * 0.5f);
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
                if (this.mActivePointerID == MyBaseOnScreenControl.INVALID_POINTER_ID) {
                    this.mActivePointerID = pointerID;
                    this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mActivePointerID == pointerID) {
                    this.mActivePointerID = MyBaseOnScreenControl.INVALID_POINTER_ID;
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
        final TiledSprite controlBase = this.mControlBase;

        final float relativeX = (MathUtils.bringToBounds(0, controlBase.getWidth(), pTouchAreaLocalX) / controlBase.getWidth()) - 0.5f;
        final float relativeY = (MathUtils.bringToBounds(0, controlBase.getHeight(), pTouchAreaLocalY) / controlBase.getHeight()) - 0.5f;

        this.onUpdateControlKnob(relativeX, relativeY);
    }

    /**
     * @param pRelativeX from <code>-0.5f</code> (left) to <code>0.5</code> (right).
     * @param pRelativeY from <code>-0.5f</code> (bottom) to <code>0.5f</code> (top).
     */
    protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
        final TiledSprite controlBase = this.mControlBase;
        final Sprite controlKnob = this.mControlKnob;

        this.mControlValueX = 2 * pRelativeX;
        this.mControlValueY = 2 * pRelativeY;

        final float x = controlBase.getWidth() * (0.5f + pRelativeX);
        final float y = controlBase.getHeight() * (0.5f + pRelativeY);

        controlKnob.setPosition(x, y);
    }

    public enum Type{
        XYD(0),XY(1),X(2),Y(3);
        Type(int n){
            number = n;
        }
        int number;
    }
    public void setType(Type type){
        this.mControlBase.setCurrentTileIndex(type.number);
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static interface IOnScreenControlListener {
        // ===========================================================
        // PhysicsConstants
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        /**
         * @param pBaseOnScreenControl
         * @param pValueX between <code>-1</code> (left) to <code>1</code> (right).
         * @param pValueY between <code>-1</code> (up) to <code>1</code> (down).
         */
        public void onControlChange(final MyBaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY);
    }
}
