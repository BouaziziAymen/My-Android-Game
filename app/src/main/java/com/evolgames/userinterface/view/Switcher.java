package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.bounds.CircularBounds;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.modifier.ease.EaseStrongIn;

public class Switcher extends Container implements Temporal {

  private static final float marginX = 8f + 4f;
  private static final float marginY = 25f + 3f;
  private final List<Image> list = new ArrayList<>();
  private final float imageWidth;
  private final List<TextureRegion> regions;
  private final Button<Controller> right, left;
  private final IntConsumer onIndexChanged;
  private SwitcherState state = SwitcherState.REST;
  private float f;
  private int index;
  private int counter;

  public Switcher(
      float x, float y, List<TextureRegion> regions, float imageWidth, IntConsumer onIndexChanged) {
    super(x, y);
    this.imageWidth = imageWidth;
    this.regions = regions;
    this.onIndexChanged = onIndexChanged;
    Image background = new Image(ResourceManager.getInstance().switcherBackground);
    background.setDepth(0);
    background.setPosition(marginX, marginY);
    Image base = new Image(ResourceManager.getInstance().switcher);
    base.setDepth(2);
    this.addElement(background);
    this.addElement(base);
    Controller controller =
        new Controller() {
          @Override
          public void init() {}
        };

    right =
        new Button<>(
            ResourceManager.getInstance().switcherRight, Button.ButtonType.OneClick, false);
    right.setBounds(new CircularBounds(right, 15f));
    right.setBehavior(
        new ButtonBehavior<Controller>(controller, right) {
          @Override
          public void informControllerButtonClicked() {
            moveForward();
          }

          @Override
          public void informControllerButtonReleased() {}
        });
    right.setPosition(40 - 3, 12 - 8);

    left =
        new Button<>(ResourceManager.getInstance().switcherLeft, Button.ButtonType.OneClick, false);
    left.setBounds(new CircularBounds(left, 15f));
    left.setBehavior(
        new ButtonBehavior<Controller>(controller, left) {
          @Override
          public void informControllerButtonClicked() {
            moveBackward();
          }

          @Override
          public void informControllerButtonReleased() {}
        });
    left.setPosition(16 - 11, 12 - 8);

    right.setDepth(3);
    addElement(right);
    left.setDepth(3);
    addElement(left);
    resetVisibility();
  }

  private void addImage(Image image) {
    image.setLimited(true);
    this.addElement(image);
    image.setDepth(1);
    list.add(image);
    image.setLowerBottomY(marginY);
  }

  private Image getCurrent() {
    int index = currentIndex();
    return list.get(index);
  }

  private int currentIndex() {
    int index = 0;
    for (int i = 0; i < (int) Math.floor(f); i++) {
      if (index == 0) {
        index = list.size() - 1;
      } else {
        index--;
      }
    }
    return index;
  }

  private Image getPrevious() {
    int index = currentIndex();
    int pIndex = (index == 0) ? list.size() - 1 : index - 1;
    return list.get(pIndex);
  }

  private void moveForward() {
    this.state = SwitcherState.FORWARD;
    if (Math.round(f) - f < 0.002f && Math.round(f) - f >= 0f) {
      this.f = Math.round(f) + 0.001f;
      correctF();
    }
    counter--;
    if (index == list.size() - 1) {
      index = 0;
    } else {
      index = index + 1;
    }
    onIndexChanged.accept(index);
  }

  private void moveBackward() {
    this.state = SwitcherState.BACKWARD;
    if (f - Math.round(f) < 0.002f && f - Math.round(f) >= 0f) {
      this.f = Math.round(f) - 0.001f;
      correctF();
    }
    counter++;
    if (index == 0) {
      index = list.size() - 1;
    } else {
      index = index - 1;
    }
    onIndexChanged.accept(index);
  }

  private void resetVisibility() {
    for (Image image : list) {
      image.setVisible(false);
    }

    if (list.size() >= 2) {
      getCurrent().setVisible(true);
      getPrevious().setVisible(true);
    }
    if (list.size() == 1) {
      getPrevious().setVisible(true);
    }
  }

  void correctF() {
    if (f > list.size()) {
      f = f - list.size();
    } else if (f < 0) {
      f = list.size() + f;
    }
  }

  @Override
  public void onStep() {
    if (list.size() == 0) {
      return;
    }
    int index = currentIndex();
    if (state == SwitcherState.FORWARD) {
      f += 0.03f;
    } else if (state == SwitcherState.BACKWARD) {
      f -= 0.03f;
    }
    correctF();
    int newIndex = currentIndex();
    if (newIndex != index) {
      if (counter > 0) {
        counter--;
      } else if (counter < 0) {
        counter++;
      }
      if (counter == 0) {
        if (state == SwitcherState.FORWARD) f = Math.round(f) - 0.001f;
        else f = Math.round(f) + 0.001f;
        state = SwitcherState.REST;
      }
    }
    resetVisibility();
    float a = EaseStrongIn.getValue(f % 1.0f) * imageWidth;
    float u1 = imageWidth - a;
    float U1 = imageWidth;
    float u2 = 0;
    float U2 = imageWidth - a;
    float p2 = a - imageWidth;

    Image right = getCurrent();
    right.setLimitX0(u2);
    right.setLimitX1(U2);
    right.setLowerBottomX(a + marginX);
    if (list.size() > 1) {
      Image left = getPrevious();
      left.setLimitX0(u1);
      left.setLimitX1(U1);
      left.setLowerBottomX(p2 + marginX);
    }
  }

  public void reset(int... usageTextureRegions) {
    if (usageTextureRegions.length > 0) {
      index = 0;
      this.onIndexChanged.accept(index);
    }
    for (Image image : list) {
      this.removeElement(image);
    }
    list.clear();
    for (int index : usageTextureRegions) {
      this.addImage(new Image(marginX, marginY, regions.get(index)));
    }
    this.f = 1f;
    this.resetVisibility();

    if (usageTextureRegions.length == 0 || usageTextureRegions.length == 1) {
      left.updateState(Button.State.DISABLED);
      right.updateState(Button.State.DISABLED);
    } else {
      left.updateState(Button.State.NORMAL);
      right.updateState(Button.State.NORMAL);
    }
  }

  enum SwitcherState {
    FORWARD,
    BACKWARD,
    REST
  }
}
