package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.OneLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractOneLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.HandField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.TargetField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField1;

public class ItemWindow extends AbstractOneLevelSectionedAdvancedWindow<BodyField, ItemField> {
    private final ItemWindowController itemWindowController;

    public ItemWindow(float pX, float pY, ItemWindowController controller) {
        super(pX, pY, 8, 8, controller);
        this.itemWindowController = controller;
        Text text = new Text("Tool Indicators:",2);
        text.setPadding(5);
        layout.addDummySection(text);
        createScroller();

    }

    public BodyField addBodyField(String name, int bodyFieldKey, boolean isActive){
        BodyField bodyField =  addPrimary(bodyFieldKey,isActive);
        bodyField.setText(name);
        return bodyField;
    }
    public TargetField addProjectileField(String name, int primaryKey, int secondaryKey){
        TargetField targetField = new TargetField(primaryKey, secondaryKey, itemWindowController);
        addSecondary(targetField,primaryKey,secondaryKey);
        targetField.setText(name);
        return targetField;
    }
    public HandField addHandField(String name, int primaryKey, int secondaryKey){
        HandField handField = new HandField(primaryKey, secondaryKey, itemWindowController);
        addSecondary(handField,primaryKey,secondaryKey);
        handField.setText(name);
        return handField;
    }

    @Override
    public BodyField createPrimary(int primaryKey) {
        return new BodyField(primaryKey,itemWindowController);
    }

    @Override
    public TargetField createSecondary(int primaryKey, int secondaryKey) {
        return null;
    }

    @Override
    protected OneLevelSectionLayout<BodyField, ItemField> createLayout() {
        return new OneLevelSectionLayout<>(12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }
}
