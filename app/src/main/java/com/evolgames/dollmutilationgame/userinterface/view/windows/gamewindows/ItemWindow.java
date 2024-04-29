package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.OneLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractOneLevelSectionedAdvancedWindow;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.BombField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.CasingField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.DragField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.FireSourceField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.LiquidSourceField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.ProjectileField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.SpecialPointField;
import com.evolgames.dollmutilationgame.R;

public class ItemWindow extends AbstractOneLevelSectionedAdvancedWindow<BodyField, ItemField> {
    private final ItemWindowController itemWindowController;

    public ItemWindow(float pX, float pY, ItemWindowController controller) {
        super(pX, pY, 8, 8, true, controller);
        this.itemWindowController = controller;
        Text text = new Text(ResourceManager.getInstance().getString(R.string.indicators_title), 2);
        text.setPadding(5);
        layout.addDummySection(text);
        createScroller();
    }

    public BodyField addBodyField(String name, int bodyFieldKey, boolean isActive) {
        BodyField bodyField = addPrimary(bodyFieldKey, isActive);
        bodyField.setText(name);
        return bodyField;
    }

    public ProjectileField addProjectileField(String name, int primaryKey, int modelId) {
        ProjectileField projectileField =
                new ProjectileField(primaryKey, modelId, itemWindowController);
        addSecondary(projectileField, primaryKey, projectileField.getSecondaryKey());
        projectileField.setText(name);
        return projectileField;
    }

    public CasingField addAmmoField(String name, int primaryKey, int modelId) {
        CasingField casingField = new CasingField(primaryKey, modelId, itemWindowController);
        addSecondary(casingField, primaryKey, casingField.getSecondaryKey());
        casingField.setText(name);
        return casingField;
    }

    public FireSourceField addFireSourceField(String name, int primaryKey, int modelId) {
        FireSourceField fireSourceField =
                new FireSourceField(primaryKey, modelId, itemWindowController);
        addSecondary(fireSourceField, primaryKey, fireSourceField.getSecondaryKey());
        fireSourceField.setText(name);
        return fireSourceField;
    }

    public LiquidSourceField addLiquidSourceField(String name, int primaryKey, int modelId) {
        LiquidSourceField liquidSourceField =
                new LiquidSourceField(primaryKey, modelId, itemWindowController);
        addSecondary(liquidSourceField, primaryKey, liquidSourceField.getSecondaryKey());
        liquidSourceField.setText(name);
        return liquidSourceField;
    }

    public BombField addBombField(String name, int primaryKey, int modelId) {
        BombField bombField = new BombField(primaryKey, modelId, itemWindowController);
        addSecondary(bombField, primaryKey, bombField.getSecondaryKey());
        bombField.setText(name);
        return bombField;
    }

    public DragField addDragField(String name, int primaryKey, int modelId) {
        DragField dragField = new DragField(primaryKey, modelId, itemWindowController);
        addSecondary(dragField, primaryKey, dragField.getSecondaryKey());
        dragField.setText(name);
        return dragField;
    }

    public SpecialPointField addSpecialPointField(String name, int primaryKey, int modelId) {
        SpecialPointField specialPointField = new SpecialPointField(primaryKey, modelId, itemWindowController);
        addSecondary(specialPointField, primaryKey, specialPointField.getSecondaryKey());
        specialPointField.setText(name);
        return specialPointField;
    }


    @Override
    public BodyField createPrimary(int primaryKey) {
        return new BodyField(primaryKey, itemWindowController);
    }

    @Override
    public ProjectileField createSecondary(int primaryKey, int secondaryKey) {
        return null;
    }

    @Override
    protected OneLevelSectionLayout<BodyField, ItemField> createLayout() {
        return new OneLevelSectionLayout<>(
                12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }
}
