package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Point;
import com.evolgames.userinterface.view.basics.Scroller;
import com.evolgames.userinterface.view.layouts.LinearLayout;

public abstract class AbstractLinearLayoutAdvancedWindow<L extends LinearLayout>
    extends AdvancedWindow<LinearLayoutAdvancedWindowController<?>> {
  private final Point visibilitySup;
  private final Point visibilityInf;
  protected L layout;
  private Scroller scroller;

  public AbstractLinearLayoutAdvancedWindow(
      float pX,
      float pY,
      int rows,
      int columns,
      boolean hasPadding,
      LinearLayoutAdvancedWindowController<?> controller) {
    super(pX, pY, rows, columns, controller, hasPadding);
    visibilitySup = new Point(0f, rows * TILE_SIDE - rows - 5f);
    visibilityInf = new Point(0f, 5f);
    addElement(visibilitySup);
    addElement(visibilityInf);
    layout = createLayout();
    layout.setDepth(2);
    layout.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
    addElement(layout);
  }

  public Scroller getScroller() {
    return scroller;
  }

  public float getVisibilitySup() {
    return visibilitySup.getAbsoluteY();
  }

  public float getVisibilityInf() {
    return visibilityInf.getAbsoluteY();
  }

  public float getLocalVisibilitySup() {
    return visibilitySup.getLowerBottomY();
  }

  public L getLayout() {
    return layout;
  }

  protected abstract L createLayout();

  public float getVisibilityZoneLength() {
    return getVisibilitySup() - getVisibilityInf();
  }

  public void createScroller() {
    if (mRows < 3) {
      throw new UnsupportedOperationException();
    }
    this.scroller =
        new Scroller(
            TILE_SIDE * mColumns - Scroller.UPPER_WIDTH / 2,
            getLocalVisibilitySup(),
            mRows - 2,
            getController(),
            getVisibilityZoneLength());
    scroller.setDepth(5);
    scroller.setWindowPartIdentifier(WindowPartIdentifier.SCROLLER);
    scroller.onHeightUpdated(getLayout().getHeight());
    addElement(scroller);
  }
}
