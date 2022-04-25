package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import android.graphics.Color;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;


public class ColorSelector extends Sprite implements UIHandler {
	public static final float TO_DEGREES = 1 / (float) Math.PI * 180;
	public Mesh mesh;
	org.andengine.util.adt.color.Color color;
	org.andengine.util.adt.color.Color initialColor;
	float rx, ry;
	private float value;
	private float alpha;
	private float saturation;
	private float hue;
	public ColorSelector(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().colorSelectorTextureRegion, ResourceManager.getInstance().vbom);

		float[] circle_vertices = new float[12 * 3 + 3];
		float RAY = 17;


		for (int i = 0; i < 12; i++) {
			float theta = (float) (2 * Math.PI * (float) i / (float) 12);//get the current angle

			circle_vertices[3 * i] = (float) (RAY * Math.cos(theta));//calculate the x component
			circle_vertices[3 * i + 1] = (float) (RAY * Math.sin(theta));//calculate the y component
			circle_vertices[3 * i + 2] = (float) (RAY * Math.sin(theta));
		}

		setReferencePosition(pX, pY);

		this.value = 100;
		this.alpha = 1f;
		this.mesh = new Mesh(207, 207, circle_vertices, 12, DrawMode.TRIANGLE_FAN, ResourceManager.getInstance().vbom);

		attachChild(this.mesh);

	}

	public void initialize(org.andengine.util.adt.color.Color color) {

		this.color = color;
		initialColor = new org.andengine.util.adt.color.Color(color);
	}

	public void setColor(float[] hsv, float alpha) {
		this.alpha = alpha;
		hue = hsv[0];
		saturation = hsv[1] * 100;
		value = hsv[2];

	}

	public void setColor(org.andengine.util.adt.color.Color color){
		this.color.set(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
		mesh.setColor(color);
	}

public void setReferencePosition(float x, float y){
    rx = x;
    ry = y;
}
	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent){
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		Vector2 vector = new Vector2(rx, ry).sub(x, y);
		float distance = vector.len();
		if(distance<97){
            this.saturation = distance/97;//FROM 0 TO 1

            this.hue = (float) Math.atan2(vector.y, -vector.x) * ColorSelector.TO_DEGREES +90;
	        if (this.hue < 0)
                this.hue += 360;
            updateColor();
	    

	}
	
		return contains(x, y);
	}
	private void updateColor(){
	int c =  Color.HSVToColor(new float[]{this.hue, this.saturation, this.value});
        this.mesh.setColor(Color.red(c)/(float)255, Color.green(c)/(float)255, Color.blue(c)/(float)255, this.alpha);
        this.color.set(Color.red(c)/(float)255, Color.green(c)/(float)255, Color.blue(c)/(float)255, this.alpha);

	}
	@Override
	public void diffuse(UISignal signal) {

		if(getParent() instanceof UIHandler){
			((UIHandler) getParent()).diffuse(signal);
		}
	}
	public void updateAlpha(float alpha) {
		this.alpha = alpha;
        updateColor();
	}
	public void updateValue(float value) {
		this.value = value;
        updateColor();
	}
	

	
	
}
