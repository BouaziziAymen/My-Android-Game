package com.evolgames.entities.mesh;

import android.opengl.GLES20;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

public class TexturedMesh extends Shape {
  // ===========================================================
  // PhysicsConstants
  // ===========================================================

  public static final int VERTEX_INDEX_X = 0;
  public static final int VERTEX_INDEX_Y = VERTEX_INDEX_X + 1;
  public static final int COLOR_INDEX = VERTEX_INDEX_Y + 1;
  public static final int TEXTURECOORDINATES_INDEX_U = COLOR_INDEX + 1;
  public static final int TEXTURECOORDINATES_INDEX_V = TEXTURECOORDINATES_INDEX_U + 1;

  public static final int VERTEX_SIZE = 5;

  public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT =
      new VertexBufferObjectAttributesBuilder(3)
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
          .add(
              ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
              ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES,
              2,
              GLES20.GL_FLOAT,
              false)
          .build();

  // ===========================================================
  // Fields
  // ===========================================================

  protected final TexturedMesh.ITexturedMeshVertexBufferObject mMeshVertexBufferObject;
  private final int mVertexCountToDraw;
  private final int mDrawMode;
  protected ITextureRegion mTextureRegion;

  // ===========================================================
  // Constructors
  // ===========================================================

  /**
   * Uses a default {@link TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject} in {@link
   * DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link
   * Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
   */
  public TexturedMesh(
      float pX,
      float pY,
      float[] pBufferData,
      int pVertexCount,
      DrawMode pDrawMode,
      VertexBufferObjectManager pVertexBufferObjectManager) {
    this(
        pX,
        pY,
        pBufferData,
        pVertexCount,
        pDrawMode,
        null,
        pVertexBufferObjectManager,
        DrawType.STATIC);
  }

  /**
   * Uses a default {@link TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject} in {@link
   * DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link
   * Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
   */
  public TexturedMesh(
      float pX,
      float pY,
      float[] pBufferData,
      int pVertexCount,
      DrawMode pDrawMode,
      ITextureRegion pTextureRegion,
      VertexBufferObjectManager pVertexBufferObjectManager) {
    this(
        pX,
        pY,
        pBufferData,
        pVertexCount,
        pDrawMode,
        pTextureRegion,
        pVertexBufferObjectManager,
        DrawType.STATIC);
  }

  /**
   * Uses a default {@link TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject} with the
   * {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
   */
  public TexturedMesh(
      float pX,
      float pY,
      float[] pBufferData,
      int pVertexCount,
      DrawMode pDrawMode,
      VertexBufferObjectManager pVertexBufferObjectManager,
      DrawType pDrawType) {
    this(pX, pY, pBufferData, pVertexCount, pDrawMode, null, pVertexBufferObjectManager, pDrawType);
  }

  /**
   * Uses a default {@link TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject} with the
   * {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
   */
  public TexturedMesh(
      float pX,
      float pY,
      float[] pBufferData,
      int pVertexCount,
      DrawMode pDrawMode,
      ITextureRegion pTextureRegion,
      VertexBufferObjectManager pVertexBufferObjectManager,
      DrawType pDrawType) {
    this(
        pX,
        pY,
        pVertexCount,
        pDrawMode,
        pTextureRegion,
        new TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject(
            pVertexBufferObjectManager,
            pBufferData,
            pVertexCount,
            pDrawType,
            true,
            VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
  }

  /**
   * Uses a default {@link TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject} with the
   * {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
   */
  public TexturedMesh(
      float pX,
      float pY,
      float[] pVertexX,
      float[] pVertexY,
      DrawMode pDrawMode,
      ITextureRegion pTextureRegion,
      VertexBufferObjectManager pVertexBufferObjectManager,
      DrawType pDrawType) {
    this(
        pX,
        pY,
        pVertexX.length,
        pDrawMode,
        pTextureRegion,
        new TexturedMesh.HighPerformanceTexturedMeshVertexBufferObject(
            pVertexBufferObjectManager,
            TexturedMesh.buildVertexList(pVertexX, pVertexY),
            pVertexX.length,
            pDrawType,
            true,
            VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
  }

  public TexturedMesh(
      float pX,
      float pY,
      int pVertexCount,
      DrawMode pDrawMode,
      ITextureRegion pTextureRegion,
      TexturedMesh.ITexturedMeshVertexBufferObject pMeshVertexBufferObject) {
    super(pX, pY, PositionColorTextureCoordinatesShaderProgram.getInstance());

    mDrawMode = pDrawMode.getDrawMode();
    mTextureRegion = pTextureRegion;
    mMeshVertexBufferObject = pMeshVertexBufferObject;
    mVertexCountToDraw = pVertexCount;

    if (pTextureRegion != null) {
      setBlendingEnabled(true);
      initBlendFunction(pTextureRegion);
      onUpdateTextureCoordinates();
    }

    onUpdateVertices();
    onUpdateColor();

    mMeshVertexBufferObject.setDirtyOnHardware();

    setBlendingEnabled(true);
  }

  // ===========================================================
  // Getter & Setter
  // ===========================================================

  // ===========================================================
  // Methods
  // ===========================================================
  protected static float[] buildVertexList(float[] pVertexX, float[] pVertexY) {
    assert pVertexX.length == pVertexY.length;

    float[] bufferData = new float[VERTEX_SIZE * pVertexX.length];
    TexturedMesh.updateVertexList(pVertexX, pVertexY, bufferData);
    return bufferData;
  }

  protected static void updateVertexList(float[] pVertexX, float[] pVertexY, float[] pBufferData) {
    for (int i = 0; i < pVertexX.length; i++) {
      pBufferData[i * VERTEX_SIZE + VERTEX_INDEX_X] = pVertexX[i];
      pBufferData[i * VERTEX_SIZE + VERTEX_INDEX_Y] = pVertexY[i];
    }
  }

  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================

  public float[] getBufferData() {
    return mMeshVertexBufferObject.getBufferData();
  }

  public ITextureRegion getTextureRegion() {
    return mTextureRegion;
  }

  @Override
  public TexturedMesh.ITexturedMeshVertexBufferObject getVertexBufferObject() {
    return mMeshVertexBufferObject;
  }

  @Override
  public void reset() {
    super.reset();
    initBlendFunction(getTextureRegion().getTexture());
  }

  @Override
  protected void preDraw(GLState pGLState, Camera pCamera) {
    super.preDraw(pGLState, pCamera);

    mTextureRegion.getTexture().bind(pGLState);
    mMeshVertexBufferObject.bind(pGLState, mShaderProgram);
  }

  @Override
  protected void draw(GLState pGLState, Camera pCamera) {
    mMeshVertexBufferObject.draw(mDrawMode, mVertexCountToDraw);
  }

  @Override
  protected void postDraw(GLState pGLState, Camera pCamera) {
    mMeshVertexBufferObject.unbind(pGLState, mShaderProgram);
    super.postDraw(pGLState, pCamera);
  }

  @Override
  protected void onUpdateColor() {
    mMeshVertexBufferObject.onUpdateColor(this);
  }

  @Override
  protected void onUpdateVertices() {
    mMeshVertexBufferObject.onUpdateVertices(this);
  }

  protected void onUpdateTextureCoordinates() {
    mMeshVertexBufferObject.onUpdateTextureCoordinates(this);
  }

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================

  public interface ITexturedMeshVertexBufferObject extends IVertexBufferObject {
    float[] getBufferData();

    void onUpdateColor(TexturedMesh pMesh);

    void onUpdateVertices(TexturedMesh pMesh);

    void onUpdateTextureCoordinates(TexturedMesh pMesh);
  }

  public static class HighPerformanceTexturedMeshVertexBufferObject
      extends HighPerformanceVertexBufferObject
      implements TexturedMesh.ITexturedMeshVertexBufferObject {

    private final int mVertexCount;

    public HighPerformanceTexturedMeshVertexBufferObject(
        VertexBufferObjectManager pVertexBufferObjectManager,
        float[] pBufferData,
        int pVertexCount,
        DrawType pDrawType,
        boolean pAutoDispose,
        VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
      super(
          pVertexBufferObjectManager,
          pBufferData,
          pDrawType,
          pAutoDispose,
          pVertexBufferObjectAttributes);

      mVertexCount = pVertexCount;
    }

    @Override
    public void onUpdateColor(TexturedMesh pMesh) {
      float[] bufferData = mBufferData;

      float packedColor = pMesh.getColor().getABGRPackedFloat();

      for (int i = 0; i < mVertexCount; i++) {
        bufferData[i * VERTEX_SIZE + COLOR_INDEX] = packedColor;
      }

      setDirtyOnHardware();
    }

    @Override
    public void onUpdateVertices(TexturedMesh pMesh) {
      // Since the buffer data is managed from the caller, we just mark the buffer data as dirty.
      setDirtyOnHardware();
    }

    @Override
    public void onUpdateTextureCoordinates(TexturedMesh pMesh) {
      float[] bufferData = mBufferData;

      ITextureRegion textureRegion = pMesh.getTextureRegion();

      float regionWidth = textureRegion.getWidth();
      float regionHeight = textureRegion.getHeight();
      float textureWidth = textureRegion.getTexture().getWidth();
      float textureHeight = textureRegion.getTexture().getHeight();

      float x0 = 0; // pMesh.getX0();
      float y0 = 0; // pMesh.getY0();
      float widthRatio = regionWidth / textureWidth;
      float heightRatio = regionHeight / textureHeight;

      for (int i = 0; i < mVertexCount; i++) {
        float x = bufferData[i * VERTEX_SIZE + VERTEX_INDEX_X];
        float y = bufferData[i * VERTEX_SIZE + VERTEX_INDEX_Y];

        float u = (x - x0) / regionWidth + 0.5f;
        float v = (y - y0) / -regionHeight + 0.5f;
        float U = u * widthRatio + textureRegion.getU();
        float V = v * heightRatio + textureRegion.getV();

        bufferData[i * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_U] = U;
        bufferData[i * VERTEX_SIZE + TEXTURECOORDINATES_INDEX_V] = V;
      }

      setDirtyOnHardware();
    }
  }
}
