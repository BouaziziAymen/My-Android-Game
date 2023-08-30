package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.OneLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractOneLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.AmmoField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BombField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.HandField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ProjectileField;

public class ItemWindow extends AbstractOneLevelSectionedAdvancedWindow<BodyField, ItemField> {
    private final ItemWindowController itemWindowController;

    public ItemWindow(float pX, float pY, ItemWindowController controller) {
        super(pX, pY, 8, 8,true, controller);
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
    public ProjectileField addProjectileField(String name, int primaryKey, int secondaryKey){
        ProjectileField projectileField = new ProjectileField(primaryKey, secondaryKey, itemWindowController);
        addSecondary(projectileField,primaryKey,secondaryKey);
        projectileField.setText(name);
        return projectileField;
    }

    public AmmoField addAmmoField(String name, int primaryKey, int secondaryKey){
        AmmoField ammoField = new AmmoField(primaryKey, secondaryKey, itemWindowController);
        addSecondary(ammoField,primaryKey,secondaryKey);
        ammoField.setText(name);
        return ammoField;
    }
    public BombField addBombField(String name, int primaryKey, int secondaryKey){
        BombField bombField = new BombField(primaryKey, secondaryKey, itemWindowController);
        addSecondary(bombField,primaryKey,secondaryKey);
        bombField.setText(name);
        return bombField;
    }


    @Override
    public BodyField createPrimary(int primaryKey) {
        return new BodyField(primaryKey,itemWindowController);
    }

    @Override
    public ProjectileField createSecondary(int primaryKey, int secondaryKey) {
        return null;
    }

    @Override
    protected OneLevelSectionLayout<BodyField, ItemField> createLayout() {
        return new OneLevelSectionLayout<>(12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }
}
