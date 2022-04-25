package is.kul.learningandengine.basics;

import is.kul.learningandengine.ResourceManager;

import java.nio.ByteOrder;
import java.util.Arrays;

import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.vbo.HighPerformanceMeshVertexBufferObject;
import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.data.constants.DataConstants;

import android.opengl.GLES20;
import android.util.Log;


public class NewHighPerformanceMeshVertexBufferObject extends MyHighPerformanceMeshVertexBufferObject
		 {
	
	public NewHighPerformanceMeshVertexBufferObject(
			VertexBufferObjectManager pVertexBufferObjectManager,
			float[] pBufferData, int pVertexCount, DrawType pDrawType,
			boolean pAutoDispose,
			VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pBufferData, pVertexCount, pDrawType,
				pAutoDispose, pVertexBufferObjectAttributes);

	
	}
	Mesh mesh;

@Override	public void draw(int pPrimitiveType, int pOffset, int pCount){

	//Log.e("info", "draw");
		GLES20.glDrawArrays(pPrimitiveType, pOffset, pCount);
	
	
}
	



	@Override
	public void onUpdateColor(Mesh pMesh) {
		 float[] bufferData = mBufferData;
	
MCMesh mesh = (MCMesh)pMesh;
this.mesh = mesh;
		float packedColor;

	int offset = 0;
//Log.e("checkb", Arrays.toString(this.mBufferData));
		for(int i=0;i<mesh.meshInfo.layersVertexCount.length;i++){
	
			 packedColor =mesh.meshInfo.colors[i].getABGRPackedFloat();
			// Log.e("checkb", ""+packedColor);
			for (int j = 0; j < mesh.meshInfo.layersVertexCount[i]; j++) {
				bufferData[(j+offset) * Mesh.VERTEX_SIZE + Mesh.COLOR_INDEX] = packedColor;
			}
			offset+=mesh.meshInfo.layersVertexCount[i];
		}
		//Log.e("checka", Arrays.toString(this.mBufferData));

        setDirtyOnHardware();
	}

	public void updateData(float[] newData) {
        this.mBufferData = newData;
        this.mByteBuffer = BufferUtils.allocateDirectByteBuffer(this.mBufferData.length * DataConstants.BYTES_PER_FLOAT);
        this.mByteBuffer.order(ByteOrder.nativeOrder());
        this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
        if(this.mesh !=null) onUpdateColor(this.mesh);
	}


	public void print() {
		
	//Log.e("info", "v:"+Arrays.toString(this.mBufferData));
		
	}


}
