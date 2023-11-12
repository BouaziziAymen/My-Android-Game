package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.view.basics.Element;
import java.util.ArrayList;
import java.util.Arrays;

public class BoxLayout extends Layout {
  private final int mRows;
  private final int mColumns;
  private final Element[][] elements;
  private int currentRow = 0;
  private int currentColumn = 0;

  public BoxLayout(float pX, float pY, float margin, int pRows, int pColumns) {
    super(pX, pY, margin);
    elements = new Element[pRows][pColumns];
    mRows = pRows;
    mColumns = pColumns;
  }

  public void removeElementAt(int row, int column) {
    this.removeElement(elements[row][column]);
  }

  @Override
  public boolean removeElement(Element e) {
    if (super.removeElement(e)) {
      ArrayList<Element> list = new ArrayList<>();
      for (int i = 0; i < mRows; i++) {
        for (int j = 0; j < mColumns; j++) {
          Element element = elements[i][j];
          if (element != null) if (element != e) list.add(element);
        }
      }
      for (Element[] element : elements) Arrays.fill(element, null);
      currentRow = 0;
      currentColumn = 0;
      for (int i = 0; i < list.size(); i++) {
        elements[currentRow][currentColumn] = list.get(i);
        incrementCounters();
      }
      updatePositions();
      return true;
    }
    return false;
  }

  private void updatePositions() {
    float y = 0;
    for (int i = 0; i < mRows; i++) {
      float height = 0;
      float x = 0;
      for (int j = 0; j < mColumns; j++) {
        Element e = elements[i][j];
        if (e == null) return;
        e.setPosition(x, y);
        x += e.getWidth() + margin;
        if (e.getHeight() > height) height = e.getHeight();
      }
      y -= (height + margin);
    }
    setUpdated(true);
  }

  private void incrementCounters() {
    currentColumn++;
    if (currentColumn == mColumns) {
      currentColumn = 0;
      currentRow++;
    }
    if (currentRow == mRows) {
      currentRow = 0;
    }
  }

  @Override
  public void addToLayout(Element e) {
    Element oldElement = elements[currentRow][currentColumn];
    if (oldElement != null) super.removeElement(oldElement);
    elements[currentRow][currentColumn] = e;

    addElement(e);
    updatePositions();
    incrementCounters();
  }
}
