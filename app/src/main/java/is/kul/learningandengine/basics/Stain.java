package is.kul.learningandengine.basics;

import java.util.ArrayList;


import is.kul.learningandengine.ResourceManager;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;



public class Stain {
Block stainBlock;
TexturedMesh stainMesh;
Vector2 position;
Stain(Block stainBlock){
	
	this.stainBlock = stainBlock;
	if(!stainBlock.MESHTRIANGLESGENERATED)
	stainBlock.computeMeshTriangles();
    this.position = stainBlock.position.cpy();
	ArrayList<Vector2> list = new ArrayList<Vector2>();
	for(Vector2 v:stainBlock.MESHTRIANGLES)list.add(v.cpy().sub(this.position));

	stainBlock.reset();
    float[] buffer = generateBuffer(list);
   // stainBlock.blockType = BlockType.IMAGE;
    this.stainMesh = new TexturedMesh(this.position.x, this.position.y, buffer, list.size(), DrawMode.TRIANGLES, ResourceManager.getInstance().imageTextureRegion, ResourceManager.getInstance().vbom);
   // stainMesh.setColor(new Color(138/255f,7/255f,7/255f));
	
}

private float[] generateBuffer(ArrayList<Vector2> list) {
	float[] buffer = new float[list.size()*5];
	for(int i=0;i<list.size();i++){
		buffer[5*i]= list.get(i).x;
		buffer[5*i+1]= list.get(i).y;
		buffer[5*i+2]= 0;
		buffer[5*i+3]= 0;
		buffer[5*i+4]= 0;
	}
	return buffer;
}
}
