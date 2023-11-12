package com.evolgames.userinterface.view.windows;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Image;

public abstract class Window extends Container {

  static final float TILE_SIDE = 32f;
  static final float PADDING_VERTICAL_DECALATION = -4;
  final int mRows;
  final int mColumns;
  final int mNumSlots;
  private final float[] slotXPositions;
  public Window(float pX, float pY, int rows, int columns, boolean hasPadding, int numberOfSlots) {
    super(pX, pY, columns * (TILE_SIDE - 1), rows * (TILE_SIDE - 1));
    mRows = rows;
    mColumns = columns;
    mNumSlots = numberOfSlots <= columns ? numberOfSlots : columns;
    Container padding = new Container();
    Container midPadding = new Container();
    Container body = new Container();
    body.setDepth(1);
    padding.setDepth(3);
    midPadding.setDepth(4);
    addElement(body);
    body.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
    addElement(padding);
    padding.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_PADDING);
    addElement(midPadding);
    midPadding.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_MID_PADDING);

    int[][] keys = new int[rows][columns];

    for (int i = 0; i < rows; i++)
      for (int j = 0; j < columns; j++) {
        if (i == 0 && j != 0 && j != columns - 1) keys[0][j] = 13;
        else if (i == rows - 1 && j != 0 && j != columns - 1) keys[i][j] = 7;
        else if (j == 0 && i != 0 && i != rows - 1) keys[i][0] = 9;
        else if (j == columns - 1 && i != 0 && i != rows - 1) keys[i][j] = 11;
        else keys[i][j] = 10;
      }
    keys[0][0] = 12;
    keys[0][columns - 1] = 14;
    keys[rows - 1][0] = 6;
    keys[rows - 1][columns - 1] = 8;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        float x = j * TILE_SIDE - j;
        float y = i * TILE_SIDE - i;
        Image image = new Image(x, y, ResourceManager.getInstance().window.get(keys[i][j]));
        body.addElement(image);
      }
    }
    slotXPositions = new float[mNumSlots];
    int counter = 0;
    if (hasPadding) {
      setHeight(getHeight() + ResourceManager.getInstance().window.get(0).getHeight());
      int begin = columns - mNumSlots;
      for (int j = 0; j < columns; j++) {
        float x = j * TILE_SIDE - j;
        float y = rows * TILE_SIDE - rows;
        int key;
        if (j == 0) key = 0;
        else if (j == columns - 1) key = 2;
        else key = 1;
        if (j >= begin) key += 15;
        Image image = new Image(x, y, ResourceManager.getInstance().window.get(key));
        padding.addElement(image);

        if (j == 0) key = 3;
        else if (j == columns - 1) key = 5;
        else key = 4;
        image = new Image(x, y - 12, ResourceManager.getInstance().window.get(key));
        // midPadding.addElement(image);

        if (j >= begin) {
          slotXPositions[counter] = x;
          counter++;
        }
      }
    }

    setWidth(mColumns * (TILE_SIDE - 1));
  }

  float[] getSlotXPositions() {
    return slotXPositions;
  }
}
