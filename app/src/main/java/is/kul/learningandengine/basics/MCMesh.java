package is.kul.learningandengine.basics;
import java.util.Arrays;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.World;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.vbo.IMeshVertexBufferObject;
import org.andengine.opengl.util.GLState;
import android.opengl.GLES20;
import android.util.Log;

public class MCMesh extends MyMesh{
public String name;
	public MeshInfo meshInfo;
	public MCMesh(float pX, float pY,float rot, MeshInfo meshInfo, String name) {
		super(pX, pY, meshInfo.getVerticesData(), meshInfo.getVerticesData().length/3, DrawMode.TRIANGLES,
				ResourceManager.getInstance().vbom);
		this.name = name;
        this.meshInfo = meshInfo;
       // setRotation(rot);

        //onUpdateColor();

     
      

	}

private boolean isUpdated;
float[] newData;

public void setUpdateColorOnly(){
	
	//Log.wtf("info"+World.onStep, "setUpdate");

    this.meshInfo.updateColors();
		
		
	
	//this.meshInfo.layersVertexCount[0] = newData.length/3;
}

public void setUpdate(){
	
	//Log.wtf("info"+World.onStep, "setUpdate");
    this.isUpdated = true;
	this.meshInfo.updateColors();
	this.meshInfo.updateVerticesCount();
	newData = this.meshInfo.getVerticesData();
	//Log.wtf("info", "updateDCGameEntity-action0");
	((NewHighPerformanceMeshVertexBufferObject) this.mMeshVertexBufferObject).updateData(this.newData);


	this.isUpdated = false;
	this.newData = null;

	//this.meshInfo.layersVertexCount[0] = newData.length/3;
}

@Override
protected void draw(GLState pGLState, Camera pCamera) {


	int offset = 0;

	for(int i = 0; i< this.meshInfo.colors.length; i++){


        setColor(this.meshInfo.colors[i]);
	mMeshVertexBufferObject.draw(GLES20.GL_TRIANGLES,offset, this.meshInfo.layersVertexCount[i]);
	offset+= this.meshInfo.layersVertexCount[i];

	
	
	}

	}


	
	
}


