package is.kul.learningandengine;

import org.andengine.engine.Engine;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.util.Log;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:28:34 - 27.03.2010
 */

public class MyEngine extends Engine {
	// ===========================================================
	// PhysicsConstants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Camera mSecondCamera;
	private final Camera mThirdCamera;
	// ===========================================================
	// Constructors
	// ===========================================================

	public MyEngine(EngineOptions pEngineOptions, Camera pSecondCamera, Camera pThirdCamera) {
		super(pEngineOptions);
        mSecondCamera = pSecondCamera;
        mThirdCamera = pThirdCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	

	public Camera getFirstCamera() {
		return mCamera;
	}

	public Camera getSecondCamera() {
		return mSecondCamera;
	}
	
	public Camera getThirdCamera() {
		return mThirdCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateScene(float pSecondsElapsed) {
		if(!this.isSplit)
		super.onUpdateScene(pSecondsElapsed);
		else{
            getSecondCamera().onUpdate(pSecondsElapsed);
            getThirdCamera().onUpdate(pSecondsElapsed);
}
}

    private boolean isSplit;
	@SuppressLint("WrongCall")
	@Override
	protected void onDrawScene(GLState pGLState, Camera pFirstCamera) {
		if (mScene != null) {
			if(this.isSplit){
			Camera secondCamera = getSecondCamera();
			Camera thirdCamera = getThirdCamera();
			int surfaceWidth = mSurfaceWidth;
			int surfaceWidthHalf = surfaceWidth >> 1;

			int surfaceHeight = mSurfaceHeight;

			pGLState.enableScissorTest();

			/* First Screen. With first camera, on the left half of the screens mWidth. */
                GLES20.glScissor(0, 0, surfaceWidthHalf, surfaceHeight);
                GLES20.glViewport(0, 0, surfaceWidthHalf-10, surfaceHeight);

                mScene.onDraw(pGLState, secondCamera);


			/* Second Screen. With second camera, on the right half of the screens mWidth. */
                GLES20.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
                GLES20.glViewport(surfaceWidthHalf+10, 0, surfaceWidthHalf-10, surfaceHeight);

                mScene.onDraw(pGLState, thirdCamera);

            } else {
				GLES20.glScissor(0, 0, mSurfaceWidth, mSurfaceHeight);
				GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);

				mScene.onDraw(pGLState, pFirstCamera);
				
				
			}
		
			
			GLES20.glScissor(0, 0, mSurfaceWidth, mSurfaceHeight);
			GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
			pFirstCamera.onDrawHUD(pGLState);
			pGLState.disableScissorTest();
		}
	}
public void setSplit(boolean isSplit){
	this.isSplit = isSplit;
}
	
	
	
	@Override
	protected Camera getCameraFromSurfaceTouchEvent(TouchEvent pTouchEvent) {
		
		
		if(!this.isSplit)
			return getFirstCamera();
		else {
			
			if(pTouchEvent.getX()< getSecondCamera().getSurfaceWidth())
			
			return getSecondCamera();
			else return getThirdCamera();
		}


	
	}

	@Override
	protected void convertSurfaceTouchEventToSceneTouchEvent(Camera pCamera, TouchEvent pSurfaceTouchEvent) {

		if (pCamera == getFirstCamera()) {
			pCamera.convertSurfaceTouchEventToSceneTouchEvent(pSurfaceTouchEvent, mSurfaceWidth, mSurfaceHeight);
		} 
		else {
			if (pCamera == getSecondCamera()) {
				pCamera.convertSurfaceTouchEventToSceneTouchEvent(pSurfaceTouchEvent, mSurfaceWidth >>1, mSurfaceHeight);
			} else if (pCamera == getThirdCamera()){
				pSurfaceTouchEvent.offset(-mSurfaceWidth >>1, 0);
				pCamera.convertSurfaceTouchEventToSceneTouchEvent(pSurfaceTouchEvent, mSurfaceWidth >>1, mSurfaceHeight);
			}
		}

	}

	@Override
	protected void onUpdateCameraSurface() {

		int surfaceWidth = mSurfaceWidth;
		int surfaceWidthHalf = surfaceWidth >> 1;
        getFirstCamera().setSurfaceSize(0, 0, mSurfaceWidth, mSurfaceHeight);
        getSecondCamera().setSurfaceSize(0, 0, surfaceWidthHalf, mSurfaceHeight);
        getThirdCamera().setSurfaceSize(surfaceWidthHalf, 0, surfaceWidthHalf, mSurfaceHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
