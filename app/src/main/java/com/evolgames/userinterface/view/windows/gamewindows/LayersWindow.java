package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.view.windows.AbstractTwoLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField1;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField1;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField1;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.TwoLevelSectionLayout;

public class LayersWindow extends AbstractTwoLevelSectionedAdvancedWindow<BodyField1,LayerField1,DecorationField1> {
    private LayerWindowController layerWindowController;
    public LayersWindow(float pX, float pY, LayerWindowController controller) {
        super(pX, pY, 8, 8,true, controller);
        layerWindowController = controller;
        Text text = new Text("Bodies:",2);
        LinearLayout mainField = new LinearLayout(LinearLayout.Direction.Horizontal);
        mainField.addToLayout(text);

        Button<LayerWindowController> addBodyButton = new Button<>(ResourceManager.getInstance().addTextureRegion,Button.ButtonType.OneClick,true);
        addBodyButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,addBodyButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onAddBodyButtonClicked();
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });
        addBodyButton.setLowerBottomY(-addBodyButton.getHeight()/2);
        mainField.addToLayout(addBodyButton);
        mainField.setHeight(addBodyButton.getHeight());
        layout.addDummySection(mainField);
        mainField.setPadding(10);
        createScroller();
    }


    @Override
    protected TwoLevelSectionLayout<BodyField1, LayerField1, DecorationField1> createLayout() {
        return new TwoLevelSectionLayout<>(12,getLocalVisibilitySup(),LinearLayout.Direction.Vertical);
    }
    public BodyField1 addBodyField(String name,int bodyFieldKey,boolean isActive){
       BodyField1 bodyField =  addPrimary(bodyFieldKey,isActive);
       bodyField.setText(name);
       return bodyField;
    }
    public LayerField1 addLayerField(String name,int bodyFieldKey,int layerFieldKey){
        LayerField1 layerField = addSecondary(bodyFieldKey,layerFieldKey);
        layerField.setText(name);
        return layerField;
    }
    public DecorationField1 addDecorationField(int bodyFieldKey,int layerFieldKey,int decorationFieldKey){
        return addTetiary(bodyFieldKey,layerFieldKey,decorationFieldKey);
    }



    @Override
    public BodyField1 createPrimary(int primaryKey) {
        return new BodyField1(primaryKey,layerWindowController);
    }

    @Override
    public LayerField1 createSecondary(int primaryKey, int secondaryKey) {
        return new LayerField1(primaryKey,secondaryKey,layerWindowController);
    }

    @Override
    public DecorationField1 createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        return new DecorationField1(primaryKey,secondaryKey,tertiaryKey,layerWindowController);
    }

}
