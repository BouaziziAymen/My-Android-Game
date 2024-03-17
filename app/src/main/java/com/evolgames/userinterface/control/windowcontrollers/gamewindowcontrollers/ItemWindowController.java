package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.Strings;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.DragShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BombField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.CasingField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.DragField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.FireSourceField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ItemField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.LiquidSourceField;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ProjectileField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemWindowController
        extends OneLevelGameWindowController<ItemWindow, BodyField, ItemField> {
    private final AtomicInteger itemCounter = new AtomicInteger();
    private ProjectileOptionController projectileOptionController;
    private FireSourceOptionController fireSourceOptionController;
    private OutlineController outlineController;
    private EditorUserInterface editorUserInterface;
    private BodyModel selectedBodyModel;
    private CasingOptionController ammoOptionController;
    private BombOptionController bombOptionController;
    private DragOptionController dragOptionController;
    private LiquidSourceOptionController liquidSourceOptionController;

    public void setFireSourceOptionController(FireSourceOptionController fireSourceOptionController) {
        this.fireSourceOptionController = fireSourceOptionController;
    }

    public void setProjectileOptionController(ProjectileOptionController projectileOptionController) {
        this.projectileOptionController = projectileOptionController;
    }

    public void setOutlineController(OutlineController outlineController) {
        this.outlineController = outlineController;
    }

    public void setAmmoOptionController(CasingOptionController ammoOptionController) {
        this.ammoOptionController = ammoOptionController;
    }

    public void setBombOptionController(BombOptionController bombOptionController) {
        this.bombOptionController = bombOptionController;
    }

    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    public List<PointImage> getSelectedModelMovables(boolean moveLimits) {
        if (selectedSecondaryField instanceof ProjectileField) {
            ProjectileModel model =
                    editorUserInterface
                            .getToolModel()
                            .getProjectileById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getProjectileShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof CasingField) {
            CasingModel model =
                    editorUserInterface
                            .getToolModel()
                            .getAmmoById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getCasingShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof BombField) {
            BombModel model =
                    editorUserInterface
                            .getToolModel()
                            .getBombById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getBombShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof FireSourceField) {
            FireSourceModel model =
                    editorUserInterface
                            .getToolModel()
                            .getFireSourceById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getFireSourceShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof LiquidSourceField) {
            LiquidSourceModel model =
                    editorUserInterface
                            .getToolModel()
                            .getLiquidSourceById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getLiquidSourceShape().getMovables(moveLimits);
        }
        if (selectedSecondaryField instanceof DragField) {
            DragModel model =
                    editorUserInterface
                            .getToolModel()
                            .getDragModelById(
                                    selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getModelId());
            return model.getDragShape().getMovables(moveLimits);
        }
        return null;
    }

    @Override
    public void onSecondaryButtonClicked(ItemField itemField) {
        super.onSecondaryButtonClicked(itemField);
        selectedSecondaryField = itemField;
        if (itemField instanceof ProjectileField) {
            ProjectileModel model =
                    editorUserInterface
                            .getToolModel()
                            .getProjectileById(itemField.getPrimaryKey(), itemField.getModelId());
            projectileOptionController.onModelUpdated(model);
            outlineController.onItemSelectionUpdated(model);
        } else if (itemField instanceof CasingField) {
            CasingModel model =
                    editorUserInterface
                            .getToolModel()
                            .getAmmoById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        } else if (itemField instanceof BombField) {
            BombModel model =
                    editorUserInterface
                            .getToolModel()
                            .getBombById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        } else if (itemField instanceof FireSourceField) {
            FireSourceModel model =
                    editorUserInterface
                            .getToolModel()
                            .getFireSourceById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        } else if (itemField instanceof LiquidSourceField) {
            LiquidSourceModel model =
                    editorUserInterface
                            .getToolModel()
                            .getLiquidSourceById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        } else if (itemField instanceof DragField) {
            DragModel model =
                    editorUserInterface
                            .getToolModel()
                            .getDragModelById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
    }

    @Override
    public void onSecondaryButtonReleased(ItemField itemField) {
        super.onSecondaryButtonReleased(itemField);
        if (itemField instanceof ProjectileField) {
            ProjectileModel model =
                    editorUserInterface
                            .getToolModel()
                            .getProjectileById(itemField.getPrimaryKey(), itemField.getModelId());
        }
        if (itemField instanceof CasingField) {
            CasingModel model =
                    editorUserInterface
                            .getToolModel()
                            .getAmmoById(itemField.getPrimaryKey(), itemField.getModelId());
            // projectileOptionController.onModelUpdated(model);
        }
        outlineController.onItemSelectionUpdated(null);
        if (selectedSecondaryField == itemField) {
            selectedSecondaryField = null;
        }
    }

    @Override
    public void onSecondaryAdded(ItemField itemField) {
        super.onSecondaryAdded(itemField);
        if (itemField instanceof ProjectileField) {
            ProjectileModel model =
                    editorUserInterface
                            .getToolModel()
                            .getProjectileById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
        if (itemField instanceof CasingField) {
            CasingModel model =
                    editorUserInterface
                            .getToolModel()
                            .getAmmoById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
        if (itemField instanceof BombField) {
            BombModel model =
                    editorUserInterface
                            .getToolModel()
                            .getBombById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
        if (itemField instanceof FireSourceField) {
            FireSourceModel model =
                    editorUserInterface
                            .getToolModel()
                            .getFireSourceById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
        if (itemField instanceof DragField) {
            DragModel model =
                    editorUserInterface
                            .getToolModel()
                            .getDragModelById(itemField.getPrimaryKey(), itemField.getModelId());
            outlineController.onItemSelectionUpdated(model);
        }
        selectedSecondaryField = itemField;
    }

    @Override
    public void onPrimaryButtonClicked(BodyField bodyField) {
        super.onPrimaryButtonClicked(bodyField);
        this.selectedBodyModel =
                editorUserInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
        outlineController.onSelectionUpdated(selectedBodyModel, null, null);
    }

    @Override
    public void onPrimaryButtonReleased(BodyField bodyField) {
        super.onPrimaryButtonReleased(bodyField);
        selectedBodyModel = null;
        outlineController.onSelectionUpdated(null, null, null);
    }

    @Override
    public void onPrimaryAdded(BodyField bodyField) {
        super.onPrimaryAdded(bodyField);
    }

    private void resetLayout() {
        for (BodyField bodyField : window.getLayout().getPrimaries()) {
            if (bodyField != null) {
                window.getLayout().removePrimary(bodyField.getPrimaryKey());
            }
        }
    }

    public void refresh() {
        resetLayout();
        ArrayList<BodyModel> bodies = editorUserInterface.getToolModel().getBodies();
        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            BodyField bodyField =
                    window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), i == 0);
            if (i == 0) {
                selectedPrimaryField = bodyField;
            }
            for (ProjectileModel projectileModel : bodyModel.getProjectileModels()) {
                onProjectileCreated(projectileModel);
            }
            for (CasingModel casingModel : bodyModel.getCasingModels()) {
                onCasingCreated(casingModel);
            }
            for (BombModel bombModel : bodyModel.getBombModels()) {
                onBombCreated(bombModel);
            }
            for (FireSourceModel fireSourceModel : bodyModel.getFireSourceModels()) {
                onFireSourceCreated(fireSourceModel);
            }
            for (LiquidSourceModel liquidSourceModel : bodyModel.getLiquidSourceModels()) {
                onLiquidSourceCreated(liquidSourceModel);
            }
            for (DragModel dragModel : bodyModel.getDragModels()) {
                onDragCreated(dragModel);
            }
        }
        BodyModel selectedBody = getSelectedBody();
        if (selectedBody != null) {
            BodyField firstBodyButton =
                    window.getLayout().getSectionByKey(selectedBody.getBodyId()).getPrimary();
            onPrimaryButtonClicked(firstBodyButton);
            firstBodyButton.getControl().updateState(Button.State.PRESSED);
        }
        updateLayout();
    }

    @Override
    public void init() {
        if (editorUserInterface.getToolModel() == null) {
            return;
        }
        refresh();
    }

    private BodyModel getSelectedBody() {
        if (selectedPrimaryField == null) {
            return null;
        }
        return editorUserInterface
                .getToolModel()
                .getBodyModelById(selectedPrimaryField.getPrimaryKey());
    }

    public void onBodyCreated(BodyModel bodyModel) {
        window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), true);
        updateLayout();
    }

    public void onProjectileCreated(ProjectileModel projectileModel) {
        ProjectileField targetField =
                window.addProjectileField(
                        projectileModel.getModelName(),
                        projectileModel.getBodyId(),
                        projectileModel.getProjectileId());
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
        } else {
            return -1;
        }
    }

    private void detachFireSourceShape(FireSourceModel fireSourceModel) {
        FireSourceShape fireSourceShape = fireSourceModel.getFireSourceShape();
        fireSourceShape.detach();
    }

    private void detachLiquidSourceShape(LiquidSourceModel liquidSourceModel) {
        LiquidSourceShape liquidSourceShape = liquidSourceModel.getLiquidSourceShape();
        liquidSourceShape.detach();
    }

    private void detachDragShape(DragModel dragModel) {
        DragShape dragShape = dragModel.getDragShape();
        dragShape.detach();
    }

    private void detachProjectileModelShape(ProjectileModel projectileModel) {
        ProjectileShape projectileShape = projectileModel.getProjectileShape();
        projectileShape.detach();
    }

    private void detachAmmoModelShape(CasingModel ammoModel) {
        CasingShape ammoShape = ammoModel.getCasingShape();
        ammoShape.detach();
    }

    private void detachBombModelShape(BombModel bombModel) {
        BombShape bombShape = bombModel.getBombShape();
        bombShape.detach();
    }

    public void onAmmoRemoveButtonReleased(CasingField casingField) {
        CasingModel ammoModel =
                editorUserInterface
                        .getToolModel()
                        .getAmmoById(casingField.getPrimaryKey(), casingField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, ammoModel.getModelName()),
                () -> {
                    if (casingField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(casingField.getPrimaryKey(), casingField.getSecondaryKey());
                    detachAmmoModelShape(ammoModel);
                    ammoOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeAmmo(ammoModel.getBodyId(), ammoModel.getCasingId());
                    updateLayout();
                });
    }

    public void onProjectileRemoveButtonReleased(ProjectileField targetField) {
        ProjectileModel projectileModel =
                editorUserInterface
                        .getToolModel()
                        .getProjectileById(targetField.getPrimaryKey(), targetField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, projectileModel.getModelName()),
                () -> {
                    if (targetField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(targetField.getPrimaryKey(), targetField.getSecondaryKey());
                    detachProjectileModelShape(projectileModel);
                    projectileOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeProjectile(projectileModel.getBodyId(), projectileModel.getProjectileId());
                    updateLayout();
                });
    }

    public void onBombRemoveButtonReleased(BombField bombField) {
        BombModel bombModel =
                editorUserInterface
                        .getToolModel()
                        .getBombById(bombField.getPrimaryKey(), bombField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, bombModel.getModelName()),
                () -> {
                    if (bombField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(bombField.getPrimaryKey(), bombField.getSecondaryKey());
                    detachBombModelShape(bombModel);
                    bombOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeBomb(bombModel.getBodyId(), bombModel.getBombId());
                    updateLayout();
                });
    }

    public void onProjectileAborted(ProjectileModel projectileModel) {
        if (selectedSecondaryField != null
                && projectileModel.getProjectileId() == selectedSecondaryField.getSecondaryKey()) {
            window
                    .getLayout()
                    .removeSecondary(
                            selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getSecondaryKey());
            detachProjectileModelShape(projectileModel);
            projectileOptionController.onModelUpdated(null);
            editorUserInterface
                    .getToolModel()
                    .removeProjectile(projectileModel.getBodyId(), projectileModel.getProjectileId());
            updateLayout();
            selectedSecondaryField = null;
        }
    }

    public void onAmmoAborted(CasingModel ammoModel) {
        if (selectedSecondaryField != null
                && ammoModel.getCasingId() == selectedSecondaryField.getSecondaryKey()) {
            window
                    .getLayout()
                    .removeSecondary(
                            selectedSecondaryField.getPrimaryKey(), selectedSecondaryField.getSecondaryKey());
            detachAmmoModelShape(ammoModel);
            projectileOptionController.onModelUpdated(null);
            editorUserInterface.getToolModel().removeAmmo(ammoModel.getBodyId(), ammoModel.getCasingId());
            updateLayout();
            selectedSecondaryField = null;
        }
    }

    public void onProjectileSettingsButtonReleased(ProjectileField targetField) {
        ProjectileModel projectileModel =
                editorUserInterface
                        .getToolModel()
                        .getProjectileById(targetField.getPrimaryKey(), targetField.getModelId());
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
        CasingField casingField =
                window.addAmmoField(
                        casingModel.getModelName(), casingModel.getBodyId(), casingModel.getCasingId());
        casingModel.setCasingField(casingField);
        updateLayout();
        // projectileOptionController.onModelUpdated(projectileModel);
        onSecondaryButtonClicked(casingField);
        casingField.getControl().updateState(Button.State.PRESSED);
        projectileOptionController.updateCasingSelectionFields();
    }

    public void onAmmoOptionButtonReleased(CasingField casingField) {
        this.ammoOptionController.onModelUpdated(
                selectedBodyModel.getCasingModelById(casingField.getSecondaryKey()));
        this.ammoOptionController.openWindow();
        unfold();
    }

    public boolean hasSelectedItem() {
        return selectedSecondaryField != null;
    }

    public void onBombCreated(BombModel bombModel) {
        BombField bombField =
                window.addBombField(bombModel.getModelName(), bombModel.getBodyId(), bombModel.getBombId());
        bombModel.setBombField(bombField);
        updateLayout();
        onSecondaryButtonClicked(bombField);
        bombField.getControl().updateState(Button.State.PRESSED);
        bombOptionController.onModelUpdated(bombModel);
    }

    public void onDragCreated(DragModel dragModel) {
        DragField dragField =
                window.addDragField(dragModel.getModelName(), dragModel.getBodyId(), dragModel.getDragId());
        dragModel.setDragField(dragField);
        updateLayout();
        onSecondaryButtonClicked(dragField);
        dragField.getControl().updateState(Button.State.PRESSED);
        // bombOptionController.onModelUpdated(dragModel);
    }

    public void onBombOptionsButtonReleased(BombField bombField) {
        this.bombOptionController.onModelUpdated(
                selectedBodyModel.getBombModelById(bombField.getSecondaryKey()));
        this.bombOptionController.openWindow();
        unfold();
    }

    public AtomicInteger getItemCounter() {
        return itemCounter;
    }

    public void onFireSourceRemoveButtonClicked(FireSourceField fireSourceField) {
        FireSourceModel fireSourceModel =
                editorUserInterface
                        .getToolModel()
                        .getFireSourceById(fireSourceField.getPrimaryKey(), fireSourceField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, fireSourceModel.getModelName()),
                () -> {
                    if (fireSourceField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(fireSourceField.getPrimaryKey(), fireSourceField.getSecondaryKey());
                    detachFireSourceShape(fireSourceModel);
                    fireSourceOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeFireSource(fireSourceModel.getBodyId(), fireSourceModel.getFireSourceId());
                    updateLayout();
                });
    }

    public void onFireSourceCreated(FireSourceModel fireSourceModel) {
        FireSourceField fireSourceField =
                window.addFireSourceField(
                        fireSourceModel.getModelName(),
                        fireSourceModel.getBodyId(),
                        fireSourceModel.getFireSourceId());
        fireSourceModel.setFireSourceField(fireSourceField);
        updateLayout();
        fireSourceOptionController.onModelUpdated(fireSourceModel);
        onSecondaryButtonClicked(fireSourceField);
        fireSourceField.getControl().updateState(Button.State.PRESSED);
    }

    public void onFireSourceOptionClicked(FireSourceField fireSourceField) {
        FireSourceModel fireSourceModel =
                editorUserInterface
                        .getToolModel()
                        .getFireSourceById(fireSourceField.getPrimaryKey(), fireSourceField.getModelId());
        fireSourceOptionController.openWindow();
        fireSourceOptionController.onModelUpdated(fireSourceModel);
        unfold();
    }

    public void onDragRemoveButtonClicked(DragField dragField) {
        DragModel dragModel =
                editorUserInterface
                        .getToolModel()
                        .getDragModelById(dragField.getPrimaryKey(), dragField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, dragModel.getModelName()),
                () -> {
                    if (dragField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(dragField.getPrimaryKey(), dragField.getSecondaryKey());
                    detachDragShape(dragModel);
                    bombOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeDrag(dragModel.getBodyId(), dragModel.getDragId());
                    updateLayout();
                });
    }

    public void onDragOptionClicked(DragField dragField) {
        DragModel dragModel =
                editorUserInterface
                        .getToolModel()
                        .getDragModelById(dragField.getPrimaryKey(), dragField.getModelId());
        dragOptionController.openWindow();
        dragOptionController.onModelUpdated(dragModel);
        unfold();
    }

    public DragOptionController getDragOptionController() {
        return dragOptionController;
    }

    public void setDragOptionController(DragOptionController dragOptionController) {
        this.dragOptionController = dragOptionController;
    }

    public void onLiquidSourceRemoveButtonClicked(LiquidSourceField liquidSourceField) {
        LiquidSourceModel liquidSourceModel =
                editorUserInterface
                        .getToolModel()
                        .getLiquidSourceById(liquidSourceField.getPrimaryKey(), liquidSourceField.getModelId());
        editorUserInterface.doWithConfirm(
                String.format(Strings.ITEM_DELETE_CONFIRM, liquidSourceModel.getModelName()),
                () -> {
                    if (liquidSourceField == selectedSecondaryField) {
                        selectedSecondaryField = null;
                    }
                    window
                            .getLayout()
                            .removeSecondary(liquidSourceField.getPrimaryKey(), liquidSourceField.getSecondaryKey());
                    detachLiquidSourceShape(liquidSourceModel);
                    liquidSourceOptionController.onModelUpdated(null);
                    editorUserInterface
                            .getToolModel()
                            .removeLiquidSource(liquidSourceModel.getBodyId(), liquidSourceModel.getLiquidSourceId());
                    updateLayout();
                });
    }

    public void onLiquidSourceOptionClicked(LiquidSourceField liquidSourceField) {
        LiquidSourceModel liquidSourceModel =
                editorUserInterface
                        .getToolModel()
                        .getLiquidSourceById(liquidSourceField.getPrimaryKey(), liquidSourceField.getModelId());
        liquidSourceOptionController.openWindow();
        liquidSourceOptionController.onModelUpdated(liquidSourceModel);
        unfold();
    }

    public void onLiquidSourceCreated(LiquidSourceModel liquidSourceModel) {
        LiquidSourceField liquidSourceField =
                window.addLiquidSourceField(
                        liquidSourceModel.getModelName(),
                        liquidSourceModel.getBodyId(),
                        liquidSourceModel.getLiquidSourceId());
        liquidSourceModel.setLiquidSourceField(liquidSourceField);
        updateLayout();
        liquidSourceOptionController.onModelUpdated(liquidSourceModel);
        onSecondaryButtonClicked(liquidSourceField);
        liquidSourceField.getControl().updateState(Button.State.PRESSED);
    }

    public void setLiquidSourceOptionController(LiquidSourceOptionController liquidSourceOptionController) {
        this.liquidSourceOptionController = liquidSourceOptionController;
    }

}
