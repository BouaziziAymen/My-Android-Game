package org.andengine.ui.activity;

import org.andengine.util.ActivityUtils;
import org.andengine.util.DialogUtils;
import org.andengine.util.call.AsyncCallable;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.andengine.util.progress.ProgressCallable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 18:35:28 - 29.08.2009
 */
public abstract class BaseActivity extends AppCompatActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void toastOnUiThread(final CharSequence pText) {
		this.toastOnUiThread(pText, Toast.LENGTH_SHORT);
	}

	public void toastOnUiThread(final CharSequence pText, final int pDuration) {
		if (ActivityUtils.isOnUiThread()) {
			Toast.makeText(BaseActivity.this, pText, pDuration).show();
		} else {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(BaseActivity.this, pText, pDuration).show();
				}
			});
		}
	}

	@Deprecated
	public void showDialogOnUiThread(final int pDialogID) {
		DialogUtils.showDialogOnUiThread(this, pDialogID);
	}

	@Deprecated
	public void showDialogOnUiThread(final int pDialogID, final Bundle pBundle) {
		DialogUtils.showDialogOnUiThread(this, pDialogID, pBundle);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog},
	 * while the {@link Callable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResourceID
	 * @param pMessageResourceID
	 * @param pErrorMessageResourceID
	 * @param pCallable
	 * @param pCallback
	 */
	protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback) {
		this.doAsync(pTitleResourceID, pMessageResourceID, pCallable, pCallback, null);
	}

	/**
	 * Performs a task in the background, showing a indeterminate {@link ProgressDialog},
	 * while the {@link Callable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResourceID
	 * @param pMessageResourceID
	 * @param pErrorMessageResourceID
	 * @param pCallable
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doAsync(this, pTitleResourceID, pMessageResourceID, pCallable, pCallback, pExceptionCallback);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog} with an ProgressBar,
	 * while the {@link AsyncCallable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResourceID
	 * @param pMessageResourceID
	 * @param pErrorMessageResourceID
	 * @param pAsyncCallable
	 * @param pCallback
	 */
	protected <T> void doProgressAsync(final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback) {
		this.doProgressAsync(pTitleResourceID, pIconResourceID, pCallable, pCallback, null);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog} with a ProgressBar,
	 * while the {@link AsyncCallable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResourceID
	 * @param pMessageResourceID
	 * @param pErrorMessageResourceID
	 * @param pAsyncCallable
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doProgressAsync(final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doProgressAsync(this, pTitleResourceID, pIconResourceID, pCallable, pCallback, pExceptionCallback);
	}

	/**
	 * Performs a task in the background, showing an indeterminate {@link ProgressDialog},
	 * while the {@link AsyncCallable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResourceID
	 * @param pMessageResourceID
	 * @param pErrorMessageResourceID
	 * @param pAsyncCallable
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doAsync(final int pTitleResourceID, final int pMessageResourceID, final AsyncCallable<T> pAsyncCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doAsync(this, pTitleResourceID, pMessageResourceID, pAsyncCallable, pCallback, pExceptionCallback);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
