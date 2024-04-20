package com.evolgames.entities.mesh.batch;

import android.opengl.GLES20;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.adt.color.ColorUtils;
import org.andengine.util.adt.transformation.Transformation;

/**
 * TODO TRY DEGENERATE TRIANGLES! TODO Check if there is this multiple-of-X-byte(?) alignment
 * optimization?
 *
 * <p>(c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:45:48 - 14.06.2011
 */
public class MeshBatch extends Shape {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    public static final int VERTEX_INDEX_X = 0;
    public static final int VERTEX_INDEX_Y = MeshBatch.VERTEX_INDEX_X + 1;
    public static final int COLOR_INDEX = MeshBatch.VERTEX_INDEX_Y + 1;

    public static final int VERTEX_SIZE = 2 + 1;
    public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT =
            new VertexBufferObjectAttributesBuilder(2)
                    .add(
                            ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
                            ShaderProgramConstants.ATTRIBUTE_POSITION,
                            2,
                            GLES20.GL_FLOAT,
                            false)
                    .add(
                            ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
                            ShaderProgramConstants.ATTRIBUTE_COLOR,
                            4,
                            GLES20.GL_UNSIGNED_BYTE,
                            true)
                    .build();
    private static final float[] VERTICES_TMP = new float[8];
    private static final Transformation TRANSFORATION_TMP = new Transformation();

    // ===========================================================
    // Fields
    // ===========================================================
    private final int mCapacity;
    private final HighPerformanceMeshBatchVertexBufferObject mMeshBatchVertexBufferObject;
    private int mIndex;
    private int mVertices;

    // ===========================================================
    // Constructors
    // ===========================================================

    public MeshBatch(
            final float pX,
            final float pY,
            final int pCapacity,
            HighPerformanceMeshBatchVertexBufferObject pSpriteBatchVertexBufferObject,
            final ShaderProgram pShaderProgram) {
        super(pX, pY, pShaderProgram);
        this.mCapacity = pCapacity;
        this.mMeshBatchVertexBufferObject = pSpriteBatchVertexBufferObject;
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

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public HighPerformanceMeshBatchVertexBufferObject getVertexBufferObject() {
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

        super.postDraw(pGLState, pCamera);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.mMeshBatchVertexBufferObject != null
                && this.mMeshBatchVertexBufferObject.isAutoDispose()
                && !this.mMeshBatchVertexBufferObject.isDisposed()) {
            this.mMeshBatchVertexBufferObject.dispose();
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void begin() {
        //		GLState.disableDepthMask(pGL); // TODO Test effect of this
    }

    protected void end() {
        //		GLState.enableDepthMask(pGL);
    }

    public void draw(float[] meshData) {
        this.assertCapacity();
        this.add(meshData);
        mIndex += meshData.length / 3;
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
            throw new IllegalStateException(
                    "This supplied pIndex: '"
                            + pIndex
                            + "' is exceeding the capacity: '"
                            + this.mCapacity
                            + "' of this "
                            + this.getClass().getSimpleName()
                            + "!");
        }
    }

    private void assertCapacity() {
        if (this.mIndex == this.mCapacity) {
            throw new IllegalStateException(
                    "This "
                            + this.getClass().getSimpleName()
                            + " has already reached its capacity ("
                            + this.mCapacity
                            + ") !");
        }
    }

    protected void add(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.addInner(x1, y1, x2, y2, x3, y3);
    }

    protected void add(float[] meshData) {
        this.addInner(meshData);
    }

    /**
     * 1-+ |X| +-2
     */
    private void addInner(float x1, float y1, float x2, float y2, float x3, float y3) {
        float pRed = 1;
        float pGreen = 1;
        float pBlue = 1;
        float pAlpha = 1;
        this.mMeshBatchVertexBufferObject.addWithPackedColor(
                x1,
                y1,
                x2,
                y2,
                x3,
                y3,
                ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
    }

    private void addInner(float[] meshData) {
        float pRed = 1;
        float pGreen = 1;
        float pBlue = 1;
        float pAlpha = 1;
        this.mMeshBatchVertexBufferObject.addWithPackedColor(
                meshData, ColorUtils.convertRGBAToABGRPackedFloat(pRed, pGreen, pBlue, pAlpha));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
