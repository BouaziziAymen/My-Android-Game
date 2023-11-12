package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class Button<C extends Controller> extends ClickableImage<C, ButtonBehavior<C>> {
  private final ButtonType buttonType;
  private final TiledTextureRegion tiledTextureRegion;
  private State state = State.NORMAL;
  private int tileIndex;

  public Button(
      TiledTextureRegion tiledTextureRegion,
      ButtonBehavior<C> behavior,
      ButtonType buttonType,
      boolean createBounds) {
    super(tiledTextureRegion, behavior, createBounds);
    this.buttonType = buttonType;
    this.tiledTextureRegion = tiledTextureRegion;
  }

  public Button(
      TiledTextureRegion tiledTextureRegion, ButtonType buttonType, boolean createBounds) {
    super(tiledTextureRegion, createBounds);
    this.buttonType = buttonType;
    this.tiledTextureRegion = tiledTextureRegion;
  }

  public Button(
      float pX,
      float pY,
      TiledTextureRegion tiledTextureRegion,
      ButtonType buttonType,
      boolean createBounds) {
    super(pX, pY, tiledTextureRegion, createBounds);
    this.buttonType = buttonType;
    this.tiledTextureRegion = tiledTextureRegion;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void updateTileIndex(State state) {
    switch (state) {
      case NORMAL:
        tileIndex = 0;
        break;
      case PRESSED:
        tileIndex = 1;
        break;
      case DISABLED:
        if (tiledTextureRegion.getTileCount() >= 3) tileIndex = 2;
        break;
    }
  }

  public void updateState(State state) {
    setState(state);
    updateTileIndex(state);
    setUpdated(true);
  }

  @Override
  public void drawSelf() {
    if (!isVisible()) return;
    image.setTextureRegion(tiledTextureRegion.getTextureRegion(tileIndex));
    image.drawSelf();
  }

  public ButtonType getType() {
    return buttonType;
  }

  public enum ButtonType {
    OneClick,
    Selector,
    OneOnly
  }

  public enum State {
    NORMAL,
    PRESSED,
    DISABLED
  }
}
