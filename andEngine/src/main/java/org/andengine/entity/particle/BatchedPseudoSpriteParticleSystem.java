package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.ColorUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:39:46 - 10.05.2012
 */
public class BatchedPseudoSpriteParticleSystem extends BlendFunctionParticleSystem<Entity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final SpriteBatch mSpriteBatch;
	private final ITextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BatchedPseudoSpriteParticleSystem(final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
	}

	public BatchedPseudoSpriteParticleSystem(final float pX, final float pY, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, new IEntityFactory<Entity>() {
			@Override
			public Entity create(final float pX, final float pY) {
				return new Entity(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight());
			}
		}, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);

		this.mTextureRegion = pTextureRegion;

		this.mSpriteBatch = new SpriteBatch(pTextureRegion.getTexture(), pParticlesMaximum, pVertexBufferObjectManager);
	}

	public BatchedPseudoSpriteParticleSystem(IEntityFactory<Entity> entityFactory, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, entityFactory, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);

		this.mTextureRegion = pTextureRegion;

		this.mSpriteBatch = new SpriteBatch(pTextureRegion.getTexture(), pParticlesMaximum, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteBatch.setIndex(0);
		IntStream.range(0,mParticlesAlive-1)
				.map(i -> (mParticlesAlive - 1 - i)).mapToObj(i->mParticles[i]).forEach(
				p->{
					final Entity entity = p.getEntity();
					final float alpha = entity.getAlpha();
					final float colorABGRPackedInt = ColorUtils.convertRGBAToABGRPackedFloat(entity.getRed() * alpha, entity.getGreen() * alpha, entity.getBlue() * alpha, alpha);
					mSpriteBatch.drawWithoutChecks(this.mTextureRegion, entity, colorABGRPackedInt);
				}
		);

		this.mSpriteBatch.submit();

		this.mSpriteBatch.onDraw(pGLState, pCamera);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
