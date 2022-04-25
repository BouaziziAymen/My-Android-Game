package is.kul.learningandengine.particlesystems;





import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class Spark extends Sprite{

private FireParticleSystem system;
private float temperature;


public Spark(float pX, float pY, FireParticleSystem fire, float temperature) {
	super(pX, pY, ResourceManager.getInstance().fireParticle, ResourceManager.getInstance().vbom);
    setSystem(fire);
    setTemperature(temperature);
}

public Spark() {
	super(0, 0, ResourceManager.getInstance().fireParticle, ResourceManager.getInstance().vbom);
}


public int getSize(){
	return this.system.getNaturalSize();
}

	public FireParticleSystem getSystem() {
		return this.system;
	}



	public void setSystem(FireParticleSystem system) {
		this.system = system;
	}

	public float getTemperature() {
		return this.temperature * getHeatFactor();
	}
	private float getHeatFactor() {
		return 1- this.age;
	}


	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	float age;
	@Override
	public void onManagedUpdate(float dt){
		super.onManagedUpdate(dt);
        this.age +=dt;
	
	}

}
