package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import android.util.Log;

import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.HandField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.TargetField;

import java.util.ArrayList;

public class ItemWindowController extends OneLevelGameWindowController<ItemWindow, BodyField, ItemField> {
    private ProjectileOptionController projectileOptionController;
    private UserInterface userInterface;

    public ItemWindowController(ProjectileOptionController projectileOptionController) {
        this.projectileOptionController = projectileOptionController;
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
            projectileOptionController.updateProjectileModel(model);
            model.getProjectileShape().select();
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
        if (userInterface.getLayersWindowController().getSelectedBodyModel() != null) {
            userInterface.getLayersWindowController().getSelectedBodyModel().deselect();
        }
        userInterface.getLayersWindowController().getBodyModel(bodyField.getPrimaryKey()).select();
    }

    @Override
    public void onPrimaryButtonReleased(BodyField bodyField) {
        super.onPrimaryButtonReleased(bodyField);
        userInterface.getLayersWindowController().getBodyModel(bodyField.getPrimaryKey()).deselect();
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
            window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), i == 0);
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
        if (userInterface.getToolModel() == null) return;
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
        projectileOptionController.updateProjectileModel(projectileModel);
        onSecondaryButtonClicked(targetField);
        targetField.getControl().updateState(Button.State.PRESSED);
    }

    public int getSelectedBodyId() {
        if (selectedPrimaryField != null)
            return selectedPrimaryField.getPrimaryKey();
        else return -1;
    }

    public void onTargetRemoveButtonClicked(TargetField targetField) {

    }

    private void detachProjectileModelShape(ProjectileModel projectileModel) {
        ProjectileShape projectileShape = projectileModel.getProjectileShape();
        projectileShape.detach();

    }

    public void onTargetRemoveButtonReleased(TargetField targetField) {
        if (targetField == selectedSecondaryField) selectedSecondaryField = null;
        window.getLayout().removeSecondary(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.updateProjectileModel(null);
        userInterface.getToolModel().removeProjectile(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        updateLayout();
    }

    public void onTargetAborted(ProjectileModel projectileModel) {
        if (selectedSecondaryField != null && projectileModel.getProjectileId() == selectedSecondaryField.getSecondaryKey())
            selectedSecondaryField = null;
        window.getLayout().removeSecondary(projectileModel.getBodyId(), projectileModel.getProjectileId());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.updateProjectileModel(null);
        userInterface.getToolModel().removeProjectile(projectileModel.getBodyId(), projectileModel.getProjectileId());
        updateLayout();
    }


    public void onTargetSettingsButtonReleased(TargetField targetField) {
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        projectileOptionController.openWindow();
        projectileOptionController.updateProjectileModel(projectileModel);
    }

    public void onHandAborted(HandModel model) {

    }

    public void onNewHandCreated(HandModel handModel) {
        window.addHandField(handModel.getModelName(), handModel.getBodyId(), handModel.getHandId());
        updateLayout();
        //  handOptionController.updateProjectileModel(handModel);
    }

    public void onHandRemoveButtonClicked(HandField handField) {

    }

    public void onHandRemoveButtonReleased(HandField handField) {
    }

    public void onHandSettingsButtonReleased(HandField handField) {

    }

    public void changeItemName(String title, int primaryKey, int secondaryKey) {
        ((ItemField)window.getLayout().getSecondary(primaryKey, secondaryKey)).getControl().setTitle(title);
    }
}
