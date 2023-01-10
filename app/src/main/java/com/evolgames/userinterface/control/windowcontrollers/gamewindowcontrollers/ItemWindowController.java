package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.toolmodels.AmmoModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.AmmoShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.AmmoField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.HandField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.TargetField;

import java.util.ArrayList;

public class ItemWindowController extends OneLevelGameWindowController<ItemWindow, BodyField, ItemField> {
    private final ProjectileOptionController projectileOptionController;
    private final OutlineController outlineController;
    private UserInterface userInterface;
    private BodyModel selectedBodyModel;

    public ItemWindowController(ProjectileOptionController projectileOptionController, OutlineController outlineController) {
        this.projectileOptionController = projectileOptionController;
        this.outlineController = outlineController;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onSecondaryButtonClicked(ItemField itemField) {
        super.onSecondaryButtonClicked(itemField);
        selectedSecondaryField = itemField;
        if (itemField instanceof TargetField) {
            ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            projectileOptionController.onModelUpdated(model);
            model.getProjectileShape().select();
        }
        if (itemField instanceof AmmoField) {
            AmmoModel model = userInterface.getToolModel().getAmmoById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
           // projectileOptionController.onModelUpdated(model);
            model.getAmmoShape().select();
        }

        if (itemField instanceof HandField) {
            HandModel model = userInterface.getToolModel().getHandById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            // handOptionController.updateHandModel(model);
            model.getHandShape().select();
        }

    }

    @Override
    public void onSecondaryButtonReleased(ItemField itemField) {
        super.onSecondaryButtonReleased(itemField);
        if (itemField instanceof TargetField) {
            ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            model.getProjectileShape().release();
        }
        if (itemField instanceof AmmoField) {
            AmmoModel model = userInterface.getToolModel().getAmmoById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            // projectileOptionController.onModelUpdated(model);
            model.getAmmoShape().release();
        }
        if (itemField instanceof HandField) {
            HandModel model = userInterface.getToolModel().getHandById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            model.getHandShape().release();
        }
        if (selectedSecondaryField == itemField){
            selectedSecondaryField = null;
        }
    }

    @Override
    public void onSecondaryAdded(ItemField itemField) {
        super.onSecondaryAdded(itemField);
        ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
        model.getProjectileShape().select();
        selectedSecondaryField = itemField;
    }

    @Override
    public void onPrimaryButtonClicked(BodyField bodyField) {
        super.onPrimaryButtonClicked(bodyField);
        this.selectedBodyModel = userInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
        outlineController.onSelectionUpdated(selectedBodyModel,null,null);
    }

    @Override
    public void onPrimaryButtonReleased(BodyField bodyField) {
        super.onPrimaryButtonReleased(bodyField);
        selectedBodyModel = null;
        outlineController.onSelectionUpdated(null,null,null);
    }

    @Override
    public void onPrimaryAdded(BodyField bodyField) {
        super.onPrimaryAdded(bodyField);
    }

    private void resetLayout() {
        for (BodyField bodyField : window.getLayout().getPrimaries()) {
            if (bodyField != null)
                window.getLayout().removePrimary(bodyField.getPrimaryKey());
        }
    }

    public void refresh() {
        resetLayout();
        ArrayList<BodyModel> bodies = userInterface.getToolModel().getBodies();
        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            BodyField bodyField = window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), i == 0);
            if(i==0){
                selectedPrimaryField = bodyField;
            }
            for(ProjectileModel projectileModel:bodyModel.getProjectiles()){
                onProjectileCreated(projectileModel);
            }
        }
        BodyModel selectedBody = getSelectedBody();
        if (selectedBody != null) {
            BodyField firstBodyButton = window.getLayout().getSectionByKey(selectedBody.getBodyId()).getPrimary();
            onPrimaryButtonClicked(firstBodyButton);
            firstBodyButton.getControl().updateState(Button.State.PRESSED);
        }
        window.getLayout().updateLayout();
    }

    @Override
    public void init() {
        if (userInterface.getToolModel() == null){
            return;
        }
        refresh();
    }

    private BodyModel getSelectedBody() {
        if (selectedPrimaryField == null) return null;
        return userInterface.getToolModel().getBodyModelById(selectedPrimaryField.getPrimaryKey());
    }

    public void onBodyCreated(BodyModel bodyModel) {
        window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), true);
        window.getLayout().updateLayout();
    }

    public void onProjectileCreated(ProjectileModel projectileModel) {
        TargetField targetField = window.addProjectileField(projectileModel.getModelName(), projectileModel.getBodyId(), projectileModel.getProjectileId());
        updateLayout();
        projectileOptionController.onModelUpdated(projectileModel);
        onSecondaryButtonClicked(targetField);
        targetField.getControl().updateState(Button.State.PRESSED);
    }

    public int getSelectedBodyId() {
        if (selectedPrimaryField != null) {
            return selectedPrimaryField.getPrimaryKey();
        }
        else {
            return -1;
        }
    }

    public void onTargetRemoveButtonClicked(TargetField targetField) {

    }

    private void detachProjectileModelShape(ProjectileModel projectileModel) {
        ProjectileShape projectileShape = projectileModel.getProjectileShape();
        projectileShape.detach();

    }
    private void detachAmmoModelShape(AmmoModel ammoModel) {
        AmmoShape ammoShape = ammoModel.getAmmoShape();
        ammoShape.detach();
    }

    public void onTargetRemoveButtonReleased(TargetField targetField) {
        if (targetField == selectedSecondaryField) selectedSecondaryField = null;
        window.getLayout().removeSecondary(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeProjectile(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        updateLayout();
    }

    public void onTargetAborted(ProjectileModel projectileModel) {
        if (selectedSecondaryField != null && projectileModel.getProjectileId() == selectedSecondaryField.getSecondaryKey())
            selectedSecondaryField = null;
        window.getLayout().removeSecondary(projectileModel.getBodyId(), projectileModel.getProjectileId());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeProjectile(projectileModel.getBodyId(), projectileModel.getProjectileId());
        updateLayout();
    }

    public void onAmmoAborted(AmmoModel ammoModel) {
        if (selectedSecondaryField != null && ammoModel.getAmmoId() == selectedSecondaryField.getSecondaryKey())
            selectedSecondaryField = null;
        window.getLayout().removeSecondary(ammoModel.getBodyId(), ammoModel.getAmmoId());
        detachAmmoModelShape(ammoModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeAmmo(ammoModel.getBodyId(), ammoModel.getAmmoId());
        updateLayout();
    }

    public void onTargetSettingsButtonReleased(TargetField targetField) {
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        projectileOptionController.openWindow();
        projectileOptionController.onModelUpdated(projectileModel);
    }

    public void onHandAborted(HandModel model) {

    }

    public void onNewHandCreated(HandModel handModel) {
        window.addHandField(handModel.getModelName(), handModel.getBodyId(), handModel.getHandId());
        updateLayout();
    }

    public void onHandRemoveButtonClicked(HandField handField) {

    }

    public void onHandRemoveButtonReleased(HandField handField) {
    }

    public void onHandSettingsButtonReleased(HandField handField) {

    }

    public void changeItemName(String title, int primaryKey, int secondaryKey) {
        window.getLayout().getSecondary(primaryKey, secondaryKey).getControl().setTitle(title);
    }

    public BodyModel getSelectedBodyModel() {
        return selectedBodyModel;
    }

    public void onAmmoCreated(AmmoModel ammoModel) {
        AmmoField ammoField = window.addAmmoField(ammoModel.getModelName(), ammoModel.getBodyId(), ammoModel.getAmmoId());
        updateLayout();
        //projectileOptionController.onModelUpdated(projectileModel);
        onSecondaryButtonClicked(ammoField);
        ammoField.getControl().updateState(Button.State.PRESSED);
        projectileOptionController.updateCasingSelectionFields();
    }

    public void onAmmoRemoveButtonClicked(AmmoField ammoField) {

    }

    public void onAmmoRemoveButtonReleased(AmmoField ammoField) {

    }

    public void onAmmoButtonReleased(AmmoField ammoField) {

    }
}
