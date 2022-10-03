package com.evolgames.entities.mesh.batch;


import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.adt.color.ColorUtils;
import org.andengine.util.adt.transformation.Transformation;

import android.opengl.GLES20;

/**
 * TODO TRY DEGENERATE TRIANGLES!
 * TODO Check if there is this multiple-of-X-byte(?) alignment optimization?
 * <p>
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:45:48 - 14.06.2011
 */
public class TexturedMeshBatch extends Shape {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================


    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = TexturedMeshBatch.VERTEX_INDEX_X + 1;
    public static final int COLOR_INDEX = TexturedMeshBatch.VERTEX_INDEX_Y + 1;
    public static final int TEXTURECOORDINATES_INDEX_U = TexturedMeshBatch.COLOR_INDEX + 1;
    public static final int TEXTURECOORDINATES_INDEX_V = TexturedMeshBatch.TEXTURECOORDINATES_INDEX_U + 1;
    public static final int VERTEX_SIZE = 2 + 1 + 2;
    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
            .add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
            .add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
            .add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
            .build();
    private static final float[] VERTICES_TMP = new float[8];
    private static final Transformation TRANSFORATION_TMP = new Transformation();

    // ===========================================================
    // Fields
    // ===========================================================
    private final int mCapacity;
    private final HighPerformanceTexturedMeshBatchVertexBufferObject mMeshBatchVertexBufferObject;
    private ITexture mTexture;
    private int mIndex;
    private int mVertices;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        this(pTexture, pCapacity, new HighPerformanceTexturedMeshBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity, pDrawType, true, TexturedMeshBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        this(pX, pY, pTexture, pCapacity, new HighPerformanceTexturedMeshBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity, pDrawType, true, TexturedMeshBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
    }

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
        this(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final ShaderProgram pShaderProgram) {
        this(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        this(pTexture, pCapacity, new HighPerformanceTexturedMeshBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity, pDrawType, true, TexturedMeshBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
        this(pX, pY, pTexture, pCapacity, new HighPerformanceTexturedMeshBatchVertexBufferObject(pVertexBufferObjectManager, pCapacity, pDrawType, true, TexturedMeshBatch.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
    }

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final HighPerformanceTexturedMeshBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
        this(pTexture, pCapacity, pSpriteBatchVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final HighPerformanceTexturedMeshBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
        this(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
    }

    public TexturedMeshBatch(final ITexture pTexture, final int pCapacity, final HighPerformanceTexturedMeshBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
        this(0, 0, pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
    }

    public TexturedMeshBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final HighPerformanceTexturedMeshBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
        super(pX, pY, pShaderProgram);

        this.mTexture = pTexture;
        this.mCapacity = pCapacity;
        this.mMeshBatchVertexBufferObject = pSpriteBatchVertexBufferObject;

        this.setBlendingEnabled(true);
        this.initBlendFunction(this.mTexture);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getCapacity() {
        return this.mCapacity;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public ITexture getTexture() {
        return this.mTexture;
    }

    public void setTexture(final ITexture pTexture) {
        this.mTexture = pTexture;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public HighPerformanceTexturedMeshBatchVertexBufferObject getVertexBufferObject() {
        return this.mMeshBatchVertexBufferObject;
    }

    @Override
    public boolean collidesWith(final IEntity pOtherEntity) {
        return false;
    }

    @Override
    public boolean contains(final float pX, final float pY) {
        return false;
    }

    @Override
    protected void onUpdateVertices() {
        /* Nothing. */
    }

    @Override
    protected void preDraw(final GLState pGLState, final Camera pCamera) {
        super.preDraw(pGLState, pCamera);

        if (this.mBlendingEnabled) {
            pGLState.enableBlend();
            pGLState.blendFunction(this.mBlendFunctionSource, this.mBlendFunctionDestination);
        }

        this.mTexture.bind(pGLState);

        this.mMeshBatchVertexBufferObject.bind(pGLState, this.mShaderProgram);
    }

    @Override
    protected void draw(final GLState pGLState, final Camera pCamera) {
        this.begin();

        this.mMeshBatchVertexBufferObject.draw(GLES20.GL_TRIANGLES, mVertices);

        this.end();
    }

    @Override
    protected void postDraw(final GLState pGLState, final Camera pCamera) {
        this.mMeshBatchVertexBufferObject.unbind(pGLState, this.mShaderProgram);

        if (this.mBlendingEnabled) {
            pGLState.disableBlend();
        }

        super.postDraw(pGLState, pCamera);
    }

    @Override
    public void reset() {
        super.reset();

        this.initBlendFunction(this.mTexture);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.mMeshBatchVertexBufferObject != null && this.mMeshBatchVertexBufferObject.isAutoDispose() && !this.mMeshBatchVertexBufferObject.isDisposed()) {
            this.mMeshBatchVertexBufferObject.dispose();
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void begin() {
        //GLState.disableDepthMask(pGL); // TODO Test effect of this
    }

    protected void end() {
        //GLState.enableDepthMask(pGL);
    }


    public void draw(final ITextureRegion pTextureRegion, float[] meshData) {
        this.assertCapacity();
        this.assertTexture(pTextureRegion);

        this.add(meshData);
        mIndex += meshData.length / 5;

    }

    public void draw(final ITextureRegion pTextureRegion, float[] meshData, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.assertCapacity();
        this.assertTexture(pTextureRegion);

        this.add(meshData, pRed, pGreen, pBlue, pAlpha);
        mIndex += meshData.length / 5;

    }


    public void submit() {
        this.onSubmit();
    }

    protected void onSubmit() {
        mVertices = mIndex;
        this.mMeshBatchVertexBufferObject.setDirtyOnHardware();

        this.mIndex = 0;
        this.mMeshBatchVertexBufferObject.setBufferDataOffset(0);
    }

    private void assertCapacity(final int pIndex) {
        if (pIndex >= this.mCapacity) {
            throw new IllegalStateException("This supplied pIndex: '" + pIndex + "' is exceeding the capacity: '" + this.mCapacity + "' of this " + this.getClass().getSimpleName() + "!");
        }
    }

    private void assertCapacity() {
        if (this.mIndex == this.mCapacity) {
            throw new IllegalStateException("This " + this.getClass().getSimpleName() + " has already reached its capacity (" + this.mCapacity + ") !");
        }
    }

    protected void assertTexture(final ITextureRegion pTextureRegion) {
        if (pTextureRegion.getTexture() != this.mTexture) {
            throw new IllegalArgumentException("The supplied Texture does match the Texture of this " + this.getClass().getSimpleName() + "!");
        }
    }


    protected void add(final ITextureRegion pTextureRegion,
                       float x1, float y1, float x2, float y2, float x3, float y3) {
        this.addInner(pTextureRegion, x1, y1, x2, y2, x3, y3);
    }

    protected void add(float[] meshData) {
        this.addInner(meshData);
    }

    protected void add(float[] meshData, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.addInner(meshData, pRed, pGreen, pBlue, pAlpha);
    }


    /**
     * 1-+
     * |X|
     * +-2
     */
    private void addInner(final ITextureRegion pTextureRegion,
                          float x1, float y1, float x2, float y2, float x3, float y3) {
        float pRed = 1;
        float pGreen = 1;
        float pBlue = 1;
        float pAlpha = 1;
        this.mMeshBatchVertexBufferObject.addWithPackedColor(pTextureRegion, x1, y1, x2, y2, x3, y3, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
    }

    private void addInner(float[] meshData) {
        float pRed = 1;
        float pGreen = 1;
        float pBlue = 1;
        float pAlpha = 1;
        this.mMeshBatchVertexBufferObject.addWithPackedColor(meshData, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
    }

    private void addInner(float[] meshData, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.mMeshBatchVertexBufferObject.addWithPackedColor(meshData, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
