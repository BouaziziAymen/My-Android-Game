package com.evolgames.entities.particles.particles;

import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FireParticle extends UncoloredSprite {
    public FireParticle(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
}
