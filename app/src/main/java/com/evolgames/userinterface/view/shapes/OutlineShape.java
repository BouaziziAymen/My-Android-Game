package com.evolgames.userinterface.view.shapes;

import com.evolgames.userinterface.model.OutlineModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Container;
import org.andengine.entity.primitive.LineLoop;

public abstract class OutlineShape<M extends OutlineModel<?>> extends Container {
  protected M outlineModel;
  protected LineLoop lineLoop;
  protected EditorUserInterface editorUserInterface;
  protected Color lineColor = Colors.white;

  public OutlineShape(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  public M getOutlineModel() {
    return outlineModel;
  }

  public void setOutlineModel(M outlineModel) {
    this.outlineModel = outlineModel;
  }

  public void setLineLoopColor(Color color) {
    if (lineLoop != null) {
      lineLoop.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }
    this.lineColor = color;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (lineLoop != null) {
      lineLoop.setVisible(visible);
    }
  }

  public abstract void onModelUpdated();

  protected abstract void updateOutlineShape();

  public abstract void dispose();
}
