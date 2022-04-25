package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.ColorUtils;

import java.util.Arrays;
import java.util.Collections;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:32:29 - 09.05.2012
 */
public class BatchedSpriteParticleSystem extends BlendFunctionParticleSystem<UncoloredSprite> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final SpriteBatch mSpriteBatch;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BatchedSpriteParticleSystem(final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
	}

	public BatchedSpriteParticleSystem(final float pX, final float pY, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, new IEntityFactory<UncoloredSprite>() {
			@Override
			public UncoloredSprite create(final float pX, final float pY) {
				/* We can create an uncolored sprite, since */
				return new UncoloredSprite(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			}
		}, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);

		this.mSpriteBatch = new SpriteBatch(pTextureRegion.getTexture(), pParticlesMaximum, pVertexBufferObjectManager);
	}
	public BatchedSpriteParticleSystem(final IEntityFactory<UncoloredSprite> ief, final IParticleEmitter pParticleEmitter, final float pRateMinimum, final float pRateMaximum, final int pParticlesMaximum, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, ief, pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum);
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

		Arrays.stream(this.mParticles).sorted(Collections.reverseOrder()).parallel().forEach(
				p->{
					final Sprite sprite = p.getEntity();
					final float alpha = sprite.getAlpha();
					final float colorABGRPackedInt = ColorUtils.convertRGBAToABGRPackedFloat(sprite.getRed() * alpha, sprite.getGreen() * alpha, sprite.getBlue() * alpha, alpha);
					mSpriteBatch.draw(sprite, colorABGRPackedInt);
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
