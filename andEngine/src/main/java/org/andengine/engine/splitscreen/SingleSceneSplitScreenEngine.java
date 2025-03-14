package org.andengine.engine.splitscreen;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:28:34 - 27.03.2010
 */
public class SingleSceneSplitScreenEngine extends FixedStepEngine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Camera mSecondCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SingleSceneSplitScreenEngine(final EngineOptions pEngineOptions, final Camera pSecondCamera) {
		super(pEngineOptions,60);
		this.mSecondCamera = pSecondCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @deprecated Instead use {@link #getFirstCamera()} or {@link #getSecondCamera()}.
	 */
	@Deprecated
	@Override
	public Camera getCamera() {
		return super.mCamera;
	}

	public Camera getFirstCamera() {
		return super.mCamera;
	}

	public Camera getSecondCamera() {
		return this.mSecondCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateScene(final float pSecondsElapsed) {
		super.onUpdateScene(pSecondsElapsed);

		this.getSecondCamera().onUpdate(pSecondsElapsed);
	}

	@Override
	protected void onDrawScene(final GLState pGLState, final Camera pFirstCamera) {
		if (super.mScene != null) {
			final Camera secondCamera = this.getSecondCamera();

			final int surfaceWidth = this.mSurfaceWidth;
			final int surfaceWidthHalf = surfaceWidth >> 1;

			final int surfaceHeight = this.mSurfaceHeight;

			pGLState.enableScissorTest();

			/* First Screen. With first camera, on the left half of the screens width. */
			{
				GLES20.glScissor(0, 0, surfaceWidth, surfaceHeight);
				GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

				super.mScene.onDraw(pGLState, pFirstCamera);
				pFirstCamera.onDrawHUD(pGLState);
			}

			/* Second Screen. With second camera, on the right half of the screens width. */
			{
				GLES20.glScissor(0, 0, 200, 200);
				GLES20.glViewport(0, 0, 200, 200);

				super.mScene.onDraw(pGLState, secondCamera);
				secondCamera.onDrawHUD(pGLState);
			}

			pGLState.disableScissorTest();
		}
	}

	@Override
	protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		if (pTouchEvent.getX() <= 200 & pTouchEvent.getY()<=200) {
			return this.getSecondCamera();
		} else {
			return this.getFirstCamera();
		}
	}

	@Override
	protected void convertSurfaceTouchEventToSceneTouchEvent(final Camera pCamera, final TouchEvent pSurfaceTouchEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth >> 1;

		if (pCamera == this.getFirstCamera()) {
			pCamera.convertSurfaceTouchEventToSceneTouchEvent(pSurfaceTouchEvent, mSurfaceWidth, this.mSurfaceHeight);
		} else {
			pCamera.convertSurfaceTouchEventToSceneTouchEvent(pSurfaceTouchEvent, 200, 200);
		}
	}

	@Override
	protected void onUpdateCameraSurface() {
		final int surfaceWidth = this.mSurfaceWidth;
		final int surfaceWidthHalf = surfaceWidth >> 1;

		this.getFirstCamera().setSurfaceSize(0, 0, surfaceWidth, this.mSurfaceHeight);
		this.getSecondCamera().setSurfaceSize(0, 0, 200, 200);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
