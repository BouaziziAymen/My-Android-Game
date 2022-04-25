package is.kul.learningandengine.particlesystems;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ExpireParticleInitializer;

import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

public class BloodParticleSystem {
	private static final float RATE_MIN    = 10;
    private static final float RATE_MAX	   = 15;
    private static final int PARTICLES_MAX = 20;
    public ParticleSystem<Sprite> particleSystem;
	public BloodParticleSystem(float x, float y){


				IParticleEmitter emitter = new CircleParticleEmitter(
					x,
						y,
					10
				);
				
				IEntityFactory<Sprite> ief = new IEntityFactory<Sprite>() {
					@Override
					public Sprite create(float pX, float pY) {
					return new Sprite(pX, pY, ResourceManager.getInstance().bloodParticle,
					ResourceManager.getInstance().vbom);
					}
					};

        this.particleSystem = new ParticleSystem<Sprite>(x,y ,ief, emitter, BloodParticleSystem.RATE_MIN, BloodParticleSystem.RATE_MAX, BloodParticleSystem.PARTICLES_MAX);


        this.particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0, 0, 0, 0));
        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(1f));
        this.particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(175/256f,17/256f, 28/256f));
		        
		        GravityParticleInitializer<Sprite> gravity = new GravityParticleInitializer<Sprite>();
		        gravity.setAccelerationY(-30*32);

        this.particleSystem.addParticleInitializer(gravity);
		       // particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<Sprite>(ResourceManager.getInstance().camera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(4f));
		        //particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(1.4f, 2f, 0.4f, 0.4f));
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>( 0.2f, 3f,1f, 0.9f));
		      
			
	}
	
}
