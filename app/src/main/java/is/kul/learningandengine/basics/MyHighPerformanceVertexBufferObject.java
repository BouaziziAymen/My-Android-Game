package is.kul.learningandengine.basics;

import is.kul.learningandengine.scene.World;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.data.constants.DataConstants;
import org.andengine.util.system.SystemUtils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Compared to a {@link LowMemoryVertexBufferObject}, the {@link HighPerformanceVertexBufferObject} uses <b><u>2x</u> the heap memory</b>,
 * at the benefit of significantly faster data buffering (<b>up to <u>5x</u> faster!</b>).
 *
 * @see {@link LowMemoryVertexBufferObject} when to prefer a {@link LowMemoryVertexBufferObject} instead of a {@link HighPerformanceVertexBufferObject}
 *
 * <p>(c) 2011 Zynga Inc.</p>
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:42:18 - 15.11.2011
 */
public class MyHighPerformanceVertexBufferObject extends MyVertexBufferObject {
	// ===========================================================
	// PhysicsConstants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public  float[] mBufferData;
	public  FloatBuffer mFloatBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MyHighPerformanceVertexBufferObject(VertexBufferObjectManager pVertexBufferObjectManager, int pCapacity, DrawType pDrawType, boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);

        mBufferData = new float[pCapacity];
		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
            mFloatBuffer = mByteBuffer.asFloatBuffer();
		} else {
            mFloatBuffer = null;
		}
	}

	public MyHighPerformanceVertexBufferObject(VertexBufferObjectManager pVertexBufferObjectManager, float[] pBufferData, DrawType pDrawType, boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pBufferData.length, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
        mBufferData = pBufferData;

		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
            mFloatBuffer = mByteBuffer.asFloatBuffer();
		} else {
            mFloatBuffer = null;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getBufferData() {
		return mBufferData;
	}

	@Override
	public int getHeapMemoryByteSize() {
		return getByteCapacity();
	}

	@Override
	public int getNativeHeapMemoryByteSize() {
		return getByteCapacity();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onBufferData() {


		//Log.e("info"+World.onStep, "onBufferData");
		// TODO Check if, and how mow this condition affects performance.
		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			// TODO Check if this is similar fast or faster than the non Honeycomb codepath.
            mFloatBuffer.position(0);
            mFloatBuffer.put(mBufferData);

			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mByteBuffer.capacity(), mByteBuffer, mUsage);
		} else {
			BufferUtils.put(mByteBuffer, mBufferData, mBufferData.length, 0);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mByteBuffer.limit(), mByteBuffer, mUsage);
		}
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
