package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.CasingField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BombField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ProjectileField;

import java.util.ArrayList;
import java.util.List;

public class ItemWindowController extends OneLevelGameWindowController<ItemWindow, BodyField, ItemField> {
    private final ProjectileOptionController projectileOptionController;
    private final OutlineController outlineController;
    private UserInterface userInterface;
    private BodyModel selectedBodyModel;
    private final CasingOptionController ammoOptionController;
    private final BombOptionController bombOptionController;

    public ItemWindowController(ProjectileOptionController projectileOptionController, CasingOptionController ammoOptionController, BombOptionController bombOptionController, OutlineController outlineController) {
        this.projectileOptionController = projectileOptionController;
        this.outlineController = outlineController;
        this.ammoOptionController = ammoOptionController;
        this.bombOptionController = bombOptionController;
        this.projectileOptionController.setItemWindowController(this);
        this.ammoOptionController.setItemWindowController(this);
        this.bombOptionController.setItemWindowController(this);
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }


    public List<PointImage> getSelectedModelMovables(boolean moveLimits){
        if (selectedSecondaryField instanceof ProjectileField) {
            ProjectileModel model = userInterface.getToolModel().getProjectileById(selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getSecondaryKey());
           return model.getProjectileShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof CasingField) {
            CasingModel model = userInterface.getToolModel().getAmmoById(selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getSecondaryKey());
            return model.getCasingShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof BombField) {
            BombModel model = userInterface.getToolModel().getBombById(selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getSecondaryKey());
            return model.getBombShape().getMovables(moveLimits);
        }
        return null;
    }
    @Override
    public void onSecondaryButtonClicked(ItemField itemField) {
        super.onSecondaryButtonClicked(itemField);
        selectedSecondaryField = itemField;
        if (itemField instanceof ProjectileField) {
            ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            projectileOptionController.onModelUpdated(model);
           outlineController.onItemSelectionUpdated(model);
        }
        else if (itemField instanceof CasingField) {
            CasingModel model = userInterface.getToolModel().getAmmoById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            outlineController.onItemSelectionUpdated(model);
        }

        else if (itemField instanceof BombField) {
            BombModel model = userInterface.getToolModel().getBombById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            outlineController.onItemSelectionUpdated(model);
        }

    }

    @Override
    public void onSecondaryButtonReleased(ItemField itemField) {
        super.onSecondaryButtonReleased(itemField);
        if (itemField instanceof ProjectileField) {
            ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());

        }
        if (itemField instanceof CasingField) {
            CasingModel model = userInterface.getToolModel().getAmmoById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
            // projectileOptionController.onModelUpdated(model);
        }
        outlineController.onItemSelectionUpdated(null);
        if (selectedSecondaryField == itemField){
            selectedSecondaryField = null;
        }
    }

    @Override
    public void onSecondaryAdded(ItemField itemField) {
        super.onSecondaryAdded(itemField);
        ProjectileModel model = userInterface.getToolModel().getProjectileById(itemField.getPrimaryKey(), itemField.getSecondaryKey());
        outlineController.onItemSelectionUpdated(model);
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
            for(CasingModel casingModel:bodyModel.getCasingModels()){
                onCasingCreated(casingModel);
            }
            for(BombModel bombModel:bodyModel.getBombModels()){
                onBombCreated(bombModel);
            }
        }
        BodyModel selectedBody = getSelectedBody();
        if (selectedBody != null) {
            BodyField firstBodyButton = window.getLayout().getSectionByKey(selectedBody.getBodyId()).getPrimary();
            onPrimaryButtonClicked(firstBodyButton);
            firstBodyButton.getControl().updateState(Button.State.PRESSED);
        }
        updateLayout();
    }

    @Override
    public void init() {
        if (userInterface.getToolModel() == null){
            return;
        }
        refresh();
    }

    private BodyModel getSelectedBody() {
        if (selectedPrimaryField == null) {
            return null;
        }
        return userInterface.getToolModel().getBodyModelById(selectedPrimaryField.getPrimaryKey());
    }

    public void onBodyCreated(BodyModel bodyModel) {
        window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), true);
        updateLayout();
    }

    public void onProjectileCreated(ProjectileModel projectileModel) {

        ProjectileField targetField = window.addProjectileField(projectileModel.getModelName(), projectileModel.getBodyId(), projectileModel.getProjectileId());
        projectileModel.setProjectileField(targetField);
        updateLayout();
       projectileOptionController.onModelUpdated(projectileModel);
       onSecondaryButtonClicked(targetField);
        targetField.getControl().updateState(Button.State.PRESSED);
        projectileOptionController.updateMissileSelectionFields();
    }

    public int getSelectedBodyId() {
        if (selectedPrimaryField != null) {
            return selectedPrimaryField.getPrimaryKey();
        }
        else {
            return -1;
        }
    }

    private void detachProjectileModelShape(ProjectileModel projectileModel) {
        ProjectileShape projectileShape = projectileModel.getProjectileShape();
        projectileShape.detach();
    }

    private void detachAmmoModelShape(CasingModel ammoModel) {
        CasingShape ammoShape = ammoModel.getCasingShape();
        ammoShape.detach();
    }

    public void onAmmoRemoveButtonReleased(CasingField casingField) {
        if (casingField == selectedSecondaryField) {
            selectedSecondaryField = null;
        }
        window.getLayout().removeSecondary(casingField.getPrimaryKey(), casingField.getSecondaryKey());
        CasingModel ammoModel = userInterface.getToolModel().getAmmoById(casingField.getPrimaryKey(), casingField.getSecondaryKey());
        detachAmmoModelShape(ammoModel);
        ammoOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeAmmo(ammoModel.getBodyId(), ammoModel.getCasingId());
        updateLayout();
    }

    public void onProjectileRemoveButtonReleased(ProjectileField targetField) {
        if (targetField == selectedSecondaryField) selectedSecondaryField = null;
        window.getLayout().removeSecondary(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeProjectile(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        updateLayout();
    }

    public void onProjectileAborted(ProjectileModel projectileModel) {
        if (selectedSecondaryField != null && projectileModel.getProjectileId() == selectedSecondaryField.getSecondaryKey())
            selectedSecondaryField = null;
        window.getLayout().removeSecondary(projectileModel.getBodyId(), projectileModel.getProjectileId());
        detachProjectileModelShape(projectileModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeProjectile(projectileModel.getBodyId(), projectileModel.getProjectileId());
        updateLayout();
    }

    public void onAmmoAborted(CasingModel ammoModel) {
        if (selectedSecondaryField != null && ammoModel.getCasingId() == selectedSecondaryField.getSecondaryKey())
            selectedSecondaryField = null;
        window.getLayout().removeSecondary(ammoModel.getBodyId(), ammoModel.getCasingId());
        detachAmmoModelShape(ammoModel);
        projectileOptionController.onModelUpdated(null);
        userInterface.getToolModel().removeAmmo(ammoModel.getBodyId(), ammoModel.getCasingId());
        updateLayout();
    }

    public void onProjectileSettingsButtonReleased(ProjectileField targetField) {
        ProjectileModel projectileModel = userInterface.getToolModel().getProjectileById(targetField.getPrimaryKey(), targetField.getSecondaryKey());
        projectileOptionController.openWindow();
        projectileOptionController.onModelUpdated(projectileModel);
        unfold();
    }

    public void changeItemName(String title, int primaryKey, int secondaryKey) {
        window.getLayout().getSecondary(primaryKey, secondaryKey).getControl().setTitle(title);
    }

    public BodyModel getSelectedBodyModel() {
        return selectedBodyModel;
    }

    public void onCasingCreated(CasingModel casingModel) {
        CasingField casingField = window.addAmmoField(casingModel.getModelName(), casingModel.getBodyId(), casingModel.getCasingId());
        casingModel.setCasingField(casingField);
        updateLayout();
        //projectileOptionController.onModelUpdated(projectileModel);
        onSecondaryButtonClicked(casingField);
        casingField.getControl().updateState(Button.State.PRESSED);
        projectileOptionController.updateCasingSelectionFields();
    }

    public void onAmmoRemoveButtonClicked(CasingField casingField) {

    }


    public void onAmmoOptionButtonReleased(CasingField casingField) {
        this.ammoOptionController.onModelUpdated(selectedBodyModel.getAmmoModelById(casingField.getSecondaryKey()));
        this.ammoOptionController.openWindow();
        unfold();
    }

    public boolean hasSelectedItem() {
        return selectedSecondaryField!=null;
    }

    public void onBombCreated(BombModel bombModel) {
        BombField bombField = window.addBombField(bombModel.getModelName(), bombModel.getBodyId(), bombModel.getBombId());
        bombModel.setBombField(bombField);
        updateLayout();
        onSecondaryButtonClicked(bombField);
        bombField.getControl().updateState(Button.State.PRESSED);
    }

    public void onBombOptionsButtonReleased(BombField bombField) {
        this.bombOptionController.onModelUpdated(selectedBodyModel.getBombModelById(bombField.getSecondaryKey()));
        this.bombOptionController.openWindow();
        unfold();
    }

    public void onBombRemoveButtonClicked(BombField bombField) {

    }

    public void onBombRemoveButtonReleased(BombField bombField) {

    }
}
