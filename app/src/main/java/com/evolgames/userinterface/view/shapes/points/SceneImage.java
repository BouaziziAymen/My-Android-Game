package com.evolgames.userinterface.view.shapes.points;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.view.basics.Image;
import org.andengine.opengl.texture.region.ITextureRegion;

public class SceneImage extends Image {

  public SceneImage(ITextureRegion pTextureRegion) {
    super(pTextureRegion);
  }

  @Override
  public void drawSelf() {
    ResourceManager.getInstance().sceneBatcher.draw(
        textureRegion,
        getAbsoluteX(),
        getAbsoluteY(),
        getWidth(),
        getHeight(),
        scaleX,
        scaleY,
        getRed(),
        getGreen(),
        getBlue(),
        1f);
  }
}
