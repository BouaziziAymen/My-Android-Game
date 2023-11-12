package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.basics.Text;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class ButtonWithText<C extends Controller> extends Button<C> {
  private final Text mText;
  private boolean selected = false;
  private Color textColor;

  public ButtonWithText(
      float pX,
      float pY,
      String pTextString,
      int pFontId,
      TiledTextureRegion tiledTextureRegion,
      ButtonType buttonType,
      boolean createBounds) {
    super(pX, pY, tiledTextureRegion, buttonType, createBounds);
    mText = new Text(0, 0, pTextString, pFontId);
    centerText();
    setWidth(Math.max(getWidth(), mText.getWidth()));
    setHeight(Math.max(getHeight(), mText.getHeight()));
    addElement(mText);
  }

  public ButtonWithText(
      String pTextString,
      int pFontId,
      TiledTextureRegion tiledTextureRegion,
      ButtonType buttonType,
      boolean createBounds) {
    this(0, 0, pTextString, pFontId, tiledTextureRegion, buttonType, createBounds);
  }

  private void centerText() {
    mText.setLowerBottomX(getWidth() / 2 - mText.getWidth() / 2);
    mText.setLowerBottomY(getHeight() / 2);
  }

  public Text getText() {
    return mText;
  }

  public String getTitle() {
    return mText.getTextString();
  }

  public void setTitle(String title) {
    mText.updateText(title);
    centerText();
    if (selected) select();
    else deselect();
  }

  private void select() {
    selected = true;
    mText.setColor(0, 1, 0);
  }

  private void deselect() {
    selected = false;
    if (textColor != null)
      mText.setColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue());
    else mText.setColor(1, 1, 1);
  }

  public void setTextColor(Color textColor) {
    this.textColor = textColor;
    mText.setColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue());
  }

  public void click() {
    select();
    updateState(State.PRESSED);
  }

  public void release() {
    deselect();
    updateState(State.NORMAL);
  }
}
