package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.properties.JuiceProperties;
import com.evolgames.entities.properties.LiquidSourceProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import java.util.ArrayList;

public class LiquidSourceOptionController extends SettingsWindowController<LiquidSourceProperties> {

  private ItemWindowController itemWindowController;
  private LiquidSourceProperties liquidSourceProperties;

  public void setItemWindowController(ItemWindowController itemWindowController) {
    this.itemWindowController = itemWindowController;
  }
  @Override
  public void init() {
    super.init();
    SectionField<LiquidSourceOptionController> liquidTypeSection =
        new SectionField<>(
            1, "Liquid", ResourceManager.getInstance().mainButtonTextureRegion, this);
    window.addPrimary(liquidTypeSection);
    createLiquidButtons();

    window.createScroller();
    updateLayout();
    window.setVisible(false);
  }

  public void createLiquidButtons() {
    ArrayList<JuiceProperties> liquids = MaterialFactory.getInstance().liquids;
    for (int i = 0; i < liquids.size(); i++) {
      ButtonWithText<LiquidSourceOptionController> liquidButton =
          new ButtonWithText<>(
              liquids.get(i).getJuiceName(),
              2,
              ResourceManager.getInstance().simpleButtonTextureRegion,
              Button.ButtonType.Selector,
              true);

      SimpleSecondary<ButtonWithText<?>> liquidField = new SimpleSecondary<>(1, i, liquidButton);
      window.addSecondary(liquidField);
      liquidButton.setBehavior(
          new ButtonBehavior<LiquidSourceOptionController>(this, liquidButton) {
            @Override
            public void informControllerButtonClicked() {
              LiquidSourceOptionController.this.onLiquidButtonClicked(liquidField);
            }

            @Override
            public void informControllerButtonReleased() {}
          });
    }
  }
  private void setLiquidNumber(int index) {
    for (int i = 0; i < window.getLayout().getSecondariesSize(1); i++) {
      SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(1, i);
      Element main = element.getMain();
      if (element.getSecondaryKey() == index) {
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).updateState(Button.State.PRESSED);
        }
      } else {
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
        }
      }
    }
  }


  private void onLiquidButtonClicked(SimpleSecondary<ButtonWithText<?>> liquidField) {
    int primaryKey = liquidField.getPrimaryKey();
    int secondaryKey = liquidField.getSecondaryKey();

    for (int i = 0; i < window.getLayout().getSecondariesSize(primaryKey); i++) {
      SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(primaryKey, i);
      if (element.getSecondaryKey() != secondaryKey) {
        Element main = element.getMain();
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
        }
      }
    }

    JuiceProperties liquid = MaterialFactory.getInstance().getLiquidByIndex(secondaryKey);
    liquidSourceProperties.setLiquid(liquid.getJuiceId());
  }

  @Override
  void onModelUpdated(ProperModel<LiquidSourceProperties> model) {
    super.onModelUpdated(model);
    if (model == null) {
      return;
    }
    this.liquidSourceProperties = model.getProperties();
    this.setLiquidNumber(model.getProperties().getLiquid());
  }

  @Override
  public void onSubmitSettings() {
    super.onSubmitSettings();
    itemWindowController.onResume();
  }

  @Override
  public void onCancelSettings() {
    super.onCancelSettings();
    itemWindowController.onResume();
  }

  @FunctionalInterface
  public interface FloatConsumer {
    void accept(float value);
  }
}
