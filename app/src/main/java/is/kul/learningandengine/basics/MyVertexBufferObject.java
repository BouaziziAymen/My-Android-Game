package is.kul.learningandengine.basics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.IDisposable;
import org.andengine.util.adt.data.constants.DataConstants;

import android.opengl.GLES20;

/**
 * TODO Extract a common base class from {@link VertexBufferObject} and {@link ZeroMemoryVertexBufferObject} (due to significant code duplication).
 * 		For naming, maybe be inspired by the java ByteBuffer naming (i.e. HeapBackedFloatArrayVertexBufferObject, StreamBufferVertexBufferObject, SharedBufferStreamVertexBufferObject).
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:22:56 - 07.04.2010
 */
public abstract class MyVertexBufferObject implements IVertexBufferObject {
	// ===========================================================
	// PhysicsConstants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected  int mCapacity;
	protected final boolean mAutoDispose;
	protected final int mUsage;
	protected ByteBuffer mByteBuffer;
	
	

	
	
	protected int mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	protected boolean mDirtyOnHardware = true;

	protected boolean mDisposed;

	protected final VertexBufferObjectManager mVertexBufferObjectManager;
	protected final VertexBufferObjectAttributes mVertexBufferObjectAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pVertexBufferObjectManager (Optional, if you manage reloading on your own.)
	 * @param pCapacity
	 * @param pDrawType
	 * @param pAutoDispose when passing <code>true</code> this {@link VertexBufferObject} loads itself to the active {@link VertexBufferObjectManager}. <b><u>WARNING:</u></b> When passing <code>false</code> one needs to take care of that by oneself!
	 * @param pVertexBufferObjectAttributes to be automatically enabled on the {@link ShaderProgram} used in {@link #bind(ShaderProgram)}.
	 */
	public MyVertexBufferObject(VertexBufferObjectManager pVertexBufferObjectManager, int pCapacity, DrawType pDrawType, boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        mVertexBufferObjectManager = pVertexBufferObjectManager;
        mCapacity = pCapacity;
        mUsage = pDrawType.getUsage();
        mAutoDispose = pAutoDispose;
        mVertexBufferObjectAttributes = pVertexBufferObjectAttributes;

        mByteBuffer = BufferUtils.allocateDirectByteBuffer(pCapacity * DataConstants.BYTES_PER_FLOAT);

        mByteBuffer.order(ByteOrder.nativeOrder());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return mVertexBufferObjectManager;
	}

	@Override
	public boolean isDisposed() {
		return mDisposed;
	}

	@Override
	public boolean isAutoDispose() {
		return mAutoDispose;
	}

	@Override
	public int getHardwareBufferID() {
		return mHardwareBufferID;
	}

	@Override
	public boolean isLoadedToHardware() {
		return mHardwareBufferID != IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	}

	@Override
	public void setNotLoadedToHardware() {
        mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
        mDirtyOnHardware = true;
	}

	@Override
	public boolean isDirtyOnHardware() {
		return mDirtyOnHardware;
	}

	@Override
	public void setDirtyOnHardware() {
        mDirtyOnHardware = true;
	}

	@Override
	public int getCapacity() {
		return mCapacity;
	}

	@Override
	public int getByteCapacity() {
		return mByteBuffer.capacity();
	}

	@Override
	public int getGPUMemoryByteSize() {
		if (isLoadedToHardware()) {
			return getByteCapacity();
		} else {
			return 0;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onBufferData();

	@Override
	public void bind(GLState pGLState) {
		if (mHardwareBufferID == IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID) {
            loadToHardware(pGLState);

			if (mVertexBufferObjectManager != null) {
                mVertexBufferObjectManager.onVertexBufferObjectLoaded(this);
			}
		}

		pGLState.bindArrayBuffer(mHardwareBufferID);

		if (mDirtyOnHardware) {
            onBufferData();

            mDirtyOnHardware = false;
		}
	}

	@Override
	public void bind(GLState pGLState, ShaderProgram pShaderProgram) {
        bind(pGLState);

		pShaderProgram.bind(pGLState, mVertexBufferObjectAttributes);
	}


	@Override
	public void unbind(GLState pGLState, ShaderProgram pShaderProgram) {
		pShaderProgram.unbind(pGLState);

//		pGLState.bindBuffer(0); // TODO Does this have an positive/negative impact on performance?
	}

	@Override
	public void unloadFromHardware(GLState pGLState) {
		pGLState.deleteArrayBuffer(mHardwareBufferID);

        mHardwareBufferID = IVertexBufferObject.HARDWARE_BUFFER_ID_INVALID;
	}

	@Override
	public void draw(int pPrimitiveType, int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, 0, pCount);
	
	}

	@Override
	public void draw(int pPrimitiveType, int pOffset, int pCount) {
		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);
	}

	@Override
	public void dispose() {
		if (!mDisposed) {
            mDisposed = true;

			if (mVertexBufferObjectManager != null) {
                mVertexBufferObjectManager.onUnloadVertexBufferObject(this);
			}

			BufferUtils.freeDirectByteBuffer(mByteBuffer);
		} else {
			throw new IDisposable.AlreadyDisposedException();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if (!mDisposed) {
            dispose();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void loadToHardware(GLState pGLState) {
        mHardwareBufferID = pGLState.generateBuffer();
        mDirtyOnHardware = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
