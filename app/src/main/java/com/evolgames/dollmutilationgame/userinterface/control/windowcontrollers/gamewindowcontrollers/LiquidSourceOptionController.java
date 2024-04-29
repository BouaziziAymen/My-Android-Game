package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.dollmutilationgame.entities.basics.Liquid;
import com.evolgames.dollmutilationgame.entities.factories.Materials;
import com.evolgames.dollmutilationgame.entities.properties.LiquidSourceProperties;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.model.BodyModel;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.LiquidSourceModel;

import java.util.List;

public class LiquidSourceOptionController extends SettingsWindowController<LiquidSourceProperties> {

    private ItemWindowController itemWindowController;
    private LiquidSourceProperties liquidSourceProperties;
    private LiquidSourceProperties copy;

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    @Override
    public void init() {
        super.init();
        SectionField<LiquidSourceOptionController> liquidTypeSection =
                new SectionField<>(
                        1, ResourceManager.getInstance().getString(R.string.liquid_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(liquidTypeSection);

        createLiquidButtons();
        createSealBodiesButtons();

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    public void createSealBodiesButtons() {
        SectionField<LiquidSourceOptionController> liquidSealSection =
                new SectionField<>(
                        2, ResourceManager.getInstance().getString(R.string.seal_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(liquidSealSection);
        ToolModel toolModel = this.editorUserInterface.getToolModel();
        if (toolModel == null) return;
        LiquidSourceModel liquidSourceModel = (LiquidSourceModel) model;
        for (int i = 0; i < toolModel.getBodies().size(); i++) {
            BodyModel bodyModel = toolModel.getBodies().get(i);
            if (bodyModel.getBodyId() == liquidSourceModel.getBodyId()) {
                continue;
            }
            ButtonWithText<LiquidSourceOptionController> bodyButton =
                    new ButtonWithText<>(
                            bodyModel.getModelName(),
                            2,
                            ResourceManager.getInstance().simpleButtonTextureRegion,
                            Button.ButtonType.Selector,
                            true);

            SimpleSecondary<ButtonWithText<?>> bodyField = new SimpleSecondary<>(2, i, bodyButton);
            window.addSecondary(bodyField);
            if (liquidSourceProperties.getSealBodyId() == bodyModel.getBodyId()) {
                bodyButton.click();
            } else {
                bodyButton.release();
            }
            bodyButton.setBehavior(
                    new ButtonBehavior<LiquidSourceOptionController>(this, bodyButton) {
                        @Override
                        public void informControllerButtonClicked() {
                            liquidSourceProperties.setSealBodyId(bodyModel.getBodyId());
                            LiquidSourceOptionController.this.onBodyButtonClicked(bodyField);
                        }

                        @Override
                        public void informControllerButtonReleased() {
                            LiquidSourceOptionController.this.onBodyButtonReleased(bodyField);
                            liquidSourceProperties.setSealBodyId(-1);
                        }
                    });
        }
    }

    private void onBodyButtonReleased(SimpleSecondary<ButtonWithText<?>> bodyField) {
        super.onSecondaryButtonReleased(bodyField);
        bodyField.getMain().release();
    }

    private void onBodyButtonClicked(SimpleSecondary<ButtonWithText<?>> liquidField) {
        super.onSecondaryButtonClicked(liquidField);
        liquidField.getMain().click();
        int size = window.getLayout().getSecondariesSize(2);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
            if (liquidField != other) {
                Element main = other.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).release();
                }
            }
        }
    }

    public void createLiquidButtons() {
        List<Liquid> liquids = Materials.liquids;
        for (int i = 0; i < liquids.size(); i++) {
            ButtonWithText<LiquidSourceOptionController> liquidButton =
                    new ButtonWithText<>(
                            liquids.get(i).getJuiceName(),
                            2,
                            ResourceManager.getInstance().simpleButtonTextureRegion,
                            Button.ButtonType.Selector,
                            true);

            SimpleSecondary<ButtonWithText<?>> liquidField = new SimpleSecondary<>(1, liquids.get(i).getLiquidId(), liquidButton);
            window.addSecondary(liquidField);
            liquidButton.setBehavior(
                    new ButtonBehavior<LiquidSourceOptionController>(this, liquidButton) {
                        @Override
                        public void informControllerButtonClicked() {
                            LiquidSourceOptionController.this.onLiquidButtonClicked(liquidField);
                        }

                        @Override
                        public void informControllerButtonReleased() {
                            LiquidSourceOptionController.this.onLiquidButtonReleased(liquidField);
                        }
                    });
        }
    }

    private void onLiquidButtonReleased(SimpleSecondary<ButtonWithText<?>> liquidField) {

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
                Element main = element.getMain();
            if (element.getSecondaryKey() != secondaryKey) {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }

        Liquid liquid = Materials.getLiquidByIndex(secondaryKey);
        liquidSourceProperties.setLiquid(liquid.getLiquidId());
    }

    @Override
    void onModelUpdated(ProperModel<LiquidSourceProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }
        this.liquidSourceProperties = model.getProperties();
        this.copy = (LiquidSourceProperties) this.liquidSourceProperties.clone();
        this.setLiquidNumber(model.getProperties().getLiquid());
        resetLayout();
        createSealBodiesButtons();
        updateLayout();
    }

    private void resetLayout() {
        for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
            if (simplePrimary.getPrimaryKey() > 1)
                window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
        }
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        itemWindowController.onResume();
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        this.model.setProperties(copy);
        itemWindowController.onResume();
    }

    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);
    }
}
