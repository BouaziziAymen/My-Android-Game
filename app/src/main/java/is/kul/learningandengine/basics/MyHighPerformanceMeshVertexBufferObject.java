package is.kul.learningandengine.basics;


import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.IMeshVertexBufferObject;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:46:51 - 28.03.2012
 */
public class MyHighPerformanceMeshVertexBufferObject extends MyHighPerformanceVertexBufferObject implements IMeshVertexBufferObject {
	// ===========================================================
	// PhysicsConstants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected  int mVertexCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MyHighPerformanceMeshVertexBufferObject(VertexBufferObjectManager pVertexBufferObjectManager, float[] pBufferData, int pVertexCount, DrawType pDrawType, boolean pAutoDispose, VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pBufferData, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);

        mVertexCount = pVertexCount;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(Mesh pMesh) {
		float[] bufferData = mBufferData;

		float packedColor = pMesh.getColor().getABGRPackedFloat();

		for (int i = 0; i < mVertexCount; i++) {
			bufferData[i * Mesh.VERTEX_SIZE + Mesh.COLOR_INDEX] = packedColor;
		}

        setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(Mesh pMesh) {
		/* Since the buffer data is managed from the caller, we just mark the buffer data as dirty. */

        setDirtyOnHardware();
	}



	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}