package is.kul.learningandengine.basics;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.HighPerformanceMeshVertexBufferObject;
import org.andengine.entity.primitive.vbo.IMeshVertexBufferObject;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;

public class MyMesh extends Mesh{

	// ===========================================================
		// Constructors
		// ===========================================================

	protected int vcount;

		/**
		 * Uses a default {@link HighPerformanceMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link MyMesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
		 */
		public MyMesh(float pX, float pY, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager) {
			this(pX, pY, 0, 0, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager);

	
		}

		/**
		 * Uses a default {@link HighPerformanceMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link MyMesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
		 */
		public MyMesh(float pX, float pY, float pWidth, float pHeight, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager) {
			this(pX, pY, pWidth, pHeight, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, DrawType.DYNAMIC);
		}

		/**
		 * Uses a default {@link HighPerformanceMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link MyMesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
		 */
		public MyMesh(float pX, float pY, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
			this(pX, pY, 0, 0, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, pDrawType);
		}

		/**
		 * Uses a default {@link HighPerformanceMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link MyMesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
		 */
		public MyMesh(float pX, float pY, float pWidth, float pHeight, float[] pBufferData, int pVertexCount, DrawMode pDrawMode, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
			this(pX, pY, pWidth, pHeight, pVertexCount, pDrawMode, new NewHighPerformanceMeshVertexBufferObject(pVertexBufferObjectManager, pBufferData, pBufferData.length/3, pDrawType, true, MyMesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));

			
		}

	
		public MyMesh(float pX, float pY, float pWidth, float pHeight, int pVertexCount, DrawMode pDrawMode, IMeshVertexBufferObject pMeshVertexBufferObject) {
			super(pX,  pY,  pWidth,  pHeight,  pVertexCount,  pDrawMode,  pMeshVertexBufferObject);

			
		}
		
		
		

		
		
		
		
		@Override
        public float[] getBufferData() {
			return mMeshVertexBufferObject.getBufferData();
		}

	
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public IMeshVertexBufferObject getVertexBufferObject() {
			return mMeshVertexBufferObject;
		}

		@Override
		protected void preDraw(GLState pGLState, Camera pCamera) {
			super.preDraw(pGLState, pCamera);

            mMeshVertexBufferObject.bind(pGLState, mShaderProgram);
		}

		@Override
		protected void draw(GLState pGLState, Camera pCamera) {
            mMeshVertexBufferObject.draw(DrawMode.TRIANGLES.mDrawMode, vcount);
		}

		@Override
		protected void postDraw(GLState pGLState, Camera pCamera) {
            mMeshVertexBufferObject.unbind(pGLState, mShaderProgram);

			super.postDraw(pGLState, pCamera);
		}

		@Override
		protected void onUpdateColor() {
            mMeshVertexBufferObject.onUpdateColor(this);
		}

		@Override
		protected void onUpdateVertices() {
            mMeshVertexBufferObject.onUpdateVertices(this);
		}
		
}
