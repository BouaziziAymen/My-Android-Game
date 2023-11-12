package com.evolgames.userinterface.view.basics;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.MyLetter;

public class Text extends Container {

  private final int fontId;
  private String textString;

  public Text(float pX, float pY, String pTextString, int pFontId) {
    super(pX, pY);
    this.textString = pTextString;
    this.fontId = pFontId;
    setHeight(ResourceManager.getInstance().getFontHeight(fontId));
    updateSelf();
  }

  public Text(String pTextString, int pFontId) {
    this(0, 0, pTextString, pFontId);
  }

  public String getTextString() {
    return textString;
  }

  public void setText(String string) {
    textString = string;
  }

  public void updateText(String textString) {
    setText(textString);
    updateSelf();
  }

  public void setColor(float pRed, float pGreen, float pBlue) {
    mRed = pRed;
    mGreen = pGreen;
    mBlue = pBlue;
    for (Element e : getContents()) e.setColor(pRed, pGreen, pBlue);
  }

  private void updateSelf() {
    clearContents();
    float x = 0;
    for (int i = 0; i < textString.length(); i++) {
      char textCharacter = textString.charAt(i);
      MyLetter letter = ResourceManager.getInstance().getLetter(fontId, textCharacter);
      if (!letter.isWhiteSpace()) {
        Image image =
            new Image(
                x + letter.getOffsetX(),
                0 - letter.getOffsetY() - getHeight() / 2,
                letter.getRegion());
        image.setColor(mRed, mGreen, mBlue);
        addElement(image);
      }
      x += letter.getAdvance();
      if (i > 0) {
        int pi = i - 1;
        char previous = textString.charAt(pi);
        x += letter.getKerning(previous);
      }
    }
    setWidth(x);

    setUpdated(true);
  }
}
