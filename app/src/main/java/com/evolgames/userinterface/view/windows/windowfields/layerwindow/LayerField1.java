package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.sections.basic.SecondaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

public class LayerField1 extends SecondaryLinearLayout {
    private final ButtonWithText<LayerWindowController> mLayerControl;
    private final Button<LayerWindowController> upArrowButton;
    private final Button<LayerWindowController> downArrowButton;
    private final float ARROW_WIDTH = 14f;
    private final float ARROW_HEIGHT = 16f;

    public LayerField1(int bodyFieldKey,int layerFieldKey, LayerWindowController controller) {
        super(bodyFieldKey,layerFieldKey,4);

        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);


        mLayerControl = new ButtonWithText<>("", 2,ResourceManager.getInstance().layerButtonTextureRegion,Button.ButtonType.Selector,true);
        RectangularBounds layerButtonBounds = (RectangularBounds)mLayerControl.getBounds();
        layerButtonBounds.setWidth(70);
        layerButtonBounds.setShiftX(23.5f);

        this.setHeight(mLayerControl.getHeight());

        addToLayout(mLayerControl);
        mLayerControl.setBehavior(new ButtonBehavior<LayerWindowController>(controller,mLayerControl) {
            @Override
            public void informControllerButtonClicked() {
             controller.onSecondaryButtonClicked(LayerField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onSecondaryButtonReleased(LayerField1.this);
            }
        });
mLayerControl.setPadding(5f);


        upArrowButton = new Button<>(105 - ARROW_HEIGHT/2, mLayerControl.getHeight()/2-ARROW_WIDTH/2, ResourceManager.getInstance().upButtonTextureRegions.get(0), Button.ButtonType.OneClick,true);
        addElement(upArrowButton);
        upArrowButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,upArrowButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onUpArrowButtonClicked(LayerField1.this);
            }
        });

        downArrowButton = new Button<>(13-8,mLayerControl.getHeight()/2-7,ResourceManager.getInstance().downButtonTextureRegions.get(0),Button.ButtonType.OneClick,true);
        addElement(downArrowButton);


        downArrowButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,downArrowButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onDownArrowButtonClicked(LayerField1.this);
            }
        });

        Button<LayerWindowController> layerAddDecorationButton = new Button<>(ResourceManager.getInstance().addTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(layerAddDecorationButton);
        layerAddDecorationButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,layerAddDecorationButton) {
            @Override
            public void informControllerButtonClicked() {
                    controller.onLayerAddDecorationClicked(LayerField1.this);
            }

            @Override
            public void informControllerButtonReleased() {

                controller.onLayerAddDecorationReleased(LayerField1.this);
            }
        });





        Button<LayerWindowController> layerOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(layerOptionsButton);
        layerOptionsButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,layerOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onLayerSettingsButtonClicked(LayerField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onLayerSettingsButtonReleased(LayerField1.this);
            }
        });


layerOptionsButton.setPadding(5f);


        Button<LayerWindowController> layerRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(layerRemoveButton);
        layerRemoveButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,layerRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onLayerRemoveButtonClicked(LayerField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onLayerRemoveButtonReleased(LayerField1.this);
            }
        });
setPadding(3f);

    }

    public ButtonWithText getLayerControl() {
        return mLayerControl;
    }

    public Button<LayerWindowController> getArrowUpButton() {
        return upArrowButton;
    }
    public Button<LayerWindowController> getArrowDownButton() {
        return downArrowButton;
    }
    public void setText(String text){
        mLayerControl.setTitle(text);
    }


}

