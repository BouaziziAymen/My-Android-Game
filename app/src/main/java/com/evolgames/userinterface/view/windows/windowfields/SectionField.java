package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController2;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class SectionField<C extends AbstractSectionedAdvancedWindowController2>
    extends SimplePrimary<ButtonWithText<C>> {

  public SectionField(
      int primaryKey, String sectionName, TiledTextureRegion textureRegion, C controller) {
    super(
        primaryKey,
        new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true));

    this.setHeight(getMain().getHeight());
    this.setWidth(getMain().getWidth());
    getMain()
        .setBehavior(
            new ButtonBehavior<C>(controller, getMain()) {
              @Override
              public void informControllerButtonClicked() {
                controller.onPrimaryButtonClicked(SectionField.this);
              }

              @Override
              public void informControllerButtonReleased() {
                controller.onPrimaryButtonReleased(SectionField.this);
              }
            });
  }
}
