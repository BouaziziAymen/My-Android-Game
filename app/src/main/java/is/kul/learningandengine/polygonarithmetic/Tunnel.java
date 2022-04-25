package is.kul.learningandengine.polygonarithmetic;


	import java.util.ArrayList;

	import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.MathUtils;
import is.kul.learningandengine.polygonarithmetic.LineEquation;

	import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
	
	import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

	public class Tunnel extends Entity{

		private VertexBufferObjectManager vbom;
	float UNIT = 1f;
	ArrayList<Vector2> list;
		public Tunnel(Vector2 C){
			
			
			float alpha = 0.1f;
			Vector2 v0 = new Vector2(C.x-alpha,C.y);
			Vector2 v1 = new Vector2(C.x+alpha,C.y);
			Vector2 v2 = new Vector2(C.x+alpha,C.y+800);
			Vector2 v3 = new Vector2(C.x-alpha,C.y+800);
			
			
	
	list = new ArrayList<Vector2>();
		list.add(v0);
		list.add(v1);
		list.add(v2);
		list.add(v3);

		}
		
		
		
		
		
		
		
		
	
}
