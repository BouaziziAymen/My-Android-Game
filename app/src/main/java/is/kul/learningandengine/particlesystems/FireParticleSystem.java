package is.kul.learningandengine.particlesystems;


import java.util.ArrayList;

import java.util.HashSet;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.basics.NRType;
import is.kul.learningandengine.basics.NonRotatingChild;
import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ColorParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ExpireParticleInitializer;
import is.kul.learningandengine.scene.GameScene;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;



public class FireParticleSystem extends NonRotatingChild
{ 
    private static final float RATE_MIN    = 6;
    private static final float RATE_MAX	   = 8;
    private static final int PARTICLES_MAX = 8;
	public ParticleSystem<Spark> particleSystem;
private ArrayList<Spark> particles;
FireParticleSystem system;
float size;

private final VelocityParticleInitializer<Spark> velocityInitializer;
public CircleParticleEmitter emitter;
public void setPositionOfEmitter(float x, float y){
    this.emitter.setCenter(x, y);
}
public int getNaturalSize(){
	return Math.round(this.size);
}

	public FireParticleSystem(float x, float y,float theta, float size, DCGameEntity parent, float average){
		super(x,y,theta,parent,NRType.EMITTER);
        this.system = this;
		this.size = size;
        this.setParticles(new ArrayList<Spark>());

        this.emitter = new CircleParticleEmitter(x, y, 10);

		IEntityFactory<Spark> ief = new IEntityFactory<Spark>() {
			@Override
			public Spark create(float pX, float pY) {
				Spark s = SparkPool.obtain(pX, pY, FireParticleSystem.this.temperature, FireParticleSystem.this.system);

                FireParticleSystem.this.getParticles().add(s);
			if(FireParticleSystem.this.getParticles().size()>8){
				SparkPool.recycle(FireParticleSystem.this.getParticles().get(8));
                FireParticleSystem.this.getParticles().remove(8);
			
			}
			return s;
			}
			};


        this.particleSystem = new ParticleSystem<>(0,0,ief,
             this.emitter,
             FireParticleSystem.RATE_MIN,
             FireParticleSystem.RATE_MAX,
             FireParticleSystem.PARTICLES_MAX
    		
		);

        attachChild(this.particleSystem);


        updateTemp(average,true);


        this.velocityInitializer = new VelocityParticleInitializer<Spark>(5,-5, 40, 30);
        this.particleSystem.addParticleInitializer(this.velocityInitializer);
        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<Spark>(0.8f));

        this.particleSystem.addParticleModifier(new ScaleParticleModifier<Spark>( 0f,0.5f,size*1f,size* 0.8f));
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<Spark>(0f,0.5f, 0.8f, 0f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<Spark>(0.5f));
      
      
	}
	public ArrayList<Spark> getParticles() {
		return this.particles;
	}
	public void setParticles(ArrayList<Spark> arrayList) {
        particles = arrayList;
	}
	float temperature;
	int degree;
	private MyColorParticleInitializer<Spark> colorInitializer;
	private ColorParticleModifier<Spark> colorModifier;
	private Color initialColor;
	public void updateTemp(float temp, boolean first){
		int pdegree = this.degree;
        this.temperature = temp;


     if(this.temperature <400) this.degree =0;
else if(this.temperature <800) this.degree =1;
else if(this.temperature <1000) this.degree =2;
else if(this.temperature <1400) this.degree =3;
else if(this.temperature <1800) this.degree =4;
else if(this.temperature <2200) this.degree =5;
else if(this.temperature <3800) this.degree =6;
else if(this.temperature <5400) this.degree =7;
else if(this.temperature <7000) this.degree =8;
else if(this.temperature <12000) this.degree =9;
else if(this.temperature <20000) this.degree =10;
else this.degree =11;
		if(pdegree!= this.degree)
            setFlameColor(first);
	
		
		
	}
public void updateSpeed(){

	Vector2 vp = getPixelCenterSpeed().mul(-1/10f);


	Vector2 t = vp.cpy().nor();


    this.velocityInitializer.setVelocity(vp.x+t.y*5, vp.x+t.x*5, vp.y+80, vp.y+80);


	
}
	
public void setFlameColor(boolean first){

    this.initialColor = GameScene.flameColors.get(this.degree);

if(this.colorInitializer ==null){
    this.colorInitializer = new MyColorParticleInitializer<Spark>
	(this.initialColor.getRed(), this.initialColor.getGreen(), this.initialColor.getBlue());

    this.particleSystem.addParticleInitializer(this.colorInitializer);
} else this.colorInitializer.updateColor(this.initialColor);

int predeg = this.degree >0 ? this.degree -1:0;
Color previous = GameScene.flameColors.get(predeg);

if(this.colorModifier !=null)
    this.particleSystem.removeParticleModifier(this.colorModifier);

    this.colorModifier = new ColorParticleModifier<Spark>
(0f, 0.5f, this.initialColor.getRed(), previous.getRed(),
        this.initialColor.getGreen(),previous.getGreen(),
        this.initialColor.getBlue(), previous.getBlue());
    this.particleSystem.addParticleModifier(this.colorModifier);






}


    
}
