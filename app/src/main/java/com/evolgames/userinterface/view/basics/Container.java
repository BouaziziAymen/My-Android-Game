package com.evolgames.userinterface.view.basics;

import java.util.ArrayList;

public class Container extends Element {
  private ArrayList<Element> contents = new ArrayList<>();

  public Container() {
    super();
  }

  public Container(float pX, float pY) {
    super(pX, pY);
  }

  public Container(float pX, float pY, float pWidth, float pHeight) {
    super(pX, pY, pWidth, pHeight);
  }

  protected void clearContents() {
    contents.clear();
  }

  public boolean removeElement(Element e) {
    return getContents().remove(e);
  }

  public ArrayList<Element> getContents() {
    return contents;
  }

  public void setContents(ArrayList<Element> contents) {
    this.contents = contents;
  }

  public void addElement(Element element) {
    element.setParent(this);
    contents.add(0, element);
  }

  public void clearElements() {
    contents.clear();
    setUpdated(true);
  }

  public void setVisible(boolean visible) {
    super.setVisible(visible);
    for (Element e : contents) {
      e.setVisible(visible);
    }
  }

  @Override
  public void drawSelf() {}

  @Override
  public void setColor(float pRed, float pGreen, float pBlue) {
    super.setColor(pRed, pGreen, pBlue);
    for (Element e : getContents()) {
      e.setColor(pRed, pGreen, pBlue);
    }
  }

  @Override
  public void updateZoom(float pZoomFactor) {
    super.updateZoom(pZoomFactor);
    for (Element e : getContents()) {
      e.updateZoom(pZoomFactor);
    }
  }

  @Override
  public void setGone(boolean gone) {
    super.setGone(gone);
    for(Element e:contents){
      e.setGone(gone);
    }
  }
}
