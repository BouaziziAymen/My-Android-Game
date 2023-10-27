package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.windowcontrollers.TwoLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.gamewindows.LayersWindow;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField;

import java.util.ArrayList;
import java.util.List;

public class LayerWindowController extends TwoLevelSectionedAdvancedWindowController<LayersWindow, BodyField, LayerField, DecorationField> {

    private final OutlineController outlineController;
    private UserInterface userInterface;
    private BodySettingsWindowController bodySettingsWindowController;
    private LayerSettingsWindowController layerSettingsWindowController;
    private DecorationSettingsWindowController decorationSettingsWindowController;
    private PointsModel<?> selectedPointsModel;

    public LayerWindowController(OutlineController outlineController) {
        this.outlineController = outlineController;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void init() {
        if (userInterface.getToolModel() == null) {
            return;
        }
        ArrayList<BodyModel> bodies = userInterface.getToolModel().getBodies();
        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            BodyField bodyField = window.addBodyField(bodyModel.getBodyId(), i == 0);
            bodyField.setText(bodyModel.getModelName());
            bodyModel.setField(bodyField);
            ArrayList<LayerModel> layers = bodyModel.getLayers();
            for (int j = 0; j < layers.size(); j++) {
                LayerModel layerModel = layers.get(j);
                LayerField layerField = window.addLayerField(bodyModel.getBodyId(), layerModel.getLayerId());
                layerModel.setField(layerField);
                layerField.setText(layerModel.getModelName());

                for (DecorationModel decorationModel : layerModel.getDecorations()) {
                    DecorationField decorationField = window.addDecorationField(bodyModel.getBodyId(), layerModel.getLayerId(), decorationModel.getDecorationId());
                    decorationModel.setField(decorationField);
                    decorationField.setText(decorationModel.getModelName());
                }
            }
        }

        for (BodyModel model : userInterface.getToolModel().getBodies()) {
            resetUpDownArrows(model.getBodyId());
        }
        updateLayout();
    }


    @Override
    public void onPrimaryButtonClicked(BodyField bodyField) {
        super.onPrimaryButtonClicked(bodyField);
        for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
            BodyField otherBodyField = window.getLayout().getPrimaryByIndex(i);
            if (otherBodyField != null)
                if (otherBodyField != bodyField) {
                    otherBodyField.getBodyControl().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(otherBodyField);
                    for (int j = 0; j < window.getLayout().getSecondariesSize(otherBodyField.getPrimaryKey()); j++) {
                        LayerField otherLayerField = window.getLayout().getSecondaryByIndex(otherBodyField.getPrimaryKey(), j);
                        otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
                        onSecondaryButtonReleased(otherLayerField);
                    }
                }
        }
        BodyModel bodyModel = userInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
        LayerModel selectedLayerModel = null;
        for (int j = 0; j < window.getLayout().getSecondariesSize(bodyField.getPrimaryKey()); j++) {
            LayerField layerField = window.getLayout().getSecondaryByIndex(bodyField.getPrimaryKey(), j);
            if (layerField.getLayerControl().getState() == Button.State.PRESSED) {
                selectedLayerModel = userInterface.getToolModel().getLayerModelById(bodyField.getPrimaryKey(), layerField.getSecondaryKey());
            }
        }
        DecorationModel selectedDecorationModel = null;
        if (selectedLayerModel != null) {
            for (int j = 0; j < window.getLayout().getTertiariesSize(selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId()); j++) {
                DecorationField decorationField = window.getLayout().getTertiaryByIndex(selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId(), j);
                if (decorationField.getDecorationControl().getState() == Button.State.PRESSED) {
                    selectedDecorationModel = userInterface.getToolModel().getDecorationModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
                }
            }
        }
        if (selectedDecorationModel != null) {
            this.selectedPointsModel = selectedDecorationModel;
        } else if (selectedLayerModel != null) {
            this.selectedPointsModel = selectedLayerModel;
        }
        outlineController.onSelectionUpdated(bodyModel, selectedLayerModel, selectedDecorationModel);
    }

    @Override
    public void onPrimaryButtonReleased(BodyField bodyField) {
        super.onPrimaryButtonReleased(bodyField);
        selectedPointsModel = null;
        outlineController.onSelectionUpdated(null, null, null);
    }

    @Override
    public void onSecondaryButtonClicked(LayerField layerField) {
        super.onSecondaryButtonClicked(layerField);
        for (int i = 0; i < window.getLayout().getSecondariesSize(layerField.getPrimaryKey()); i++) {
            LayerField otherLayerField = window.getLayout().getSecondaryByIndex(layerField.getPrimaryKey(), i);
            if (otherLayerField != layerField) {
                otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
                onSecondaryButtonReleased(otherLayerField);
            }
        }
        layerField.getLayerControl().click();
        BodyModel selectedBodyModel = userInterface.getToolModel().getBodyModelById(layerField.getPrimaryKey());
        LayerModel selectedLayerModel = userInterface.getToolModel().getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        DecorationModel selectedDecorationModel = null;
        for (int j = 0; j < window.getLayout().getTertiariesSize(selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId()); j++) {
            DecorationField decorationField = window.getLayout().getTertiaryByIndex(selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId(), j);
            if (decorationField.getDecorationControl().getState() == Button.State.PRESSED) {
                selectedDecorationModel = userInterface.getToolModel().getDecorationModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
            }
        }
        if (selectedDecorationModel != null) {
            this.selectedPointsModel = selectedDecorationModel;
        } else {
            this.selectedPointsModel = selectedLayerModel;
        }

        layerSettingsWindowController.onModelUpdated(selectedLayerModel);
        outlineController.onSelectionUpdated(selectedBodyModel, selectedLayerModel, selectedDecorationModel);
    }

    @Override
    public void onSecondaryButtonReleased(LayerField layerField) {
        super.onSecondaryButtonReleased(layerField);
        BodyModel selectedBodyModel = userInterface.getToolModel().getBodyModelById(layerField.getPrimaryKey());
        layerField.getLayerControl().release();
        selectedPointsModel = null;
        outlineController.onSelectionUpdated(selectedBodyModel, null, null);
    }

    @Override
    public void onTertiaryButtonClicked(DecorationField decorationField) {
        super.onTertiaryButtonClicked(decorationField);

        BodyModel selectedBodyModel = userInterface.getToolModel().getBodyModelById(decorationField.getPrimaryKey());
        LayerModel selectedLayerModel = userInterface.getToolModel().getLayerModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey());
        DecorationModel selectedDecorationModel = userInterface.getToolModel().getDecorationModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        for (int i = 0; i < window.getLayout().getTertiariesSize(decorationField.getPrimaryKey(), decorationField.getSecondaryKey()); i++) {
            DecorationField otherDecorationField = window.getLayout().getTertiaryByIndex(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), i);
            if (otherDecorationField != decorationField) {
                otherDecorationField.getDecorationControl().updateState(Button.State.NORMAL);
                onTertiaryButtonReleased(otherDecorationField);
            }
        }
        this.selectedPointsModel = selectedDecorationModel;
        outlineController.onSelectionUpdated(selectedBodyModel, selectedLayerModel, selectedDecorationModel);
    }

    @Override
    public void onTertiaryButtonReleased(DecorationField decorationField) {
        super.onTertiaryButtonReleased(decorationField);
        BodyModel selectedBodyModel = userInterface.getToolModel().getBodyModelById(decorationField.getPrimaryKey());
        LayerModel selectedLayerModel = userInterface.getToolModel().getLayerModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey());
        this.selectedPointsModel = null;
        outlineController.onSelectionUpdated(selectedBodyModel, selectedLayerModel, null);
    }

    public void onAddBodyButtonClicked() {
        BodyModel bodyModel = userInterface.getToolModel().createNewBody();
        BodyField addedBodyField = window.addBodyField(bodyModel.getBodyId(), false);
        bodyModel.setField(addedBodyField);
        addedBodyField.setText(bodyModel.getModelName());
        ItemWindowController itemWindowController = userInterface.getItemWindowController();
        itemWindowController.onBodyCreated(bodyModel);
        userInterface.getJointSettingsWindowController().updateBodySelectionFields();
        this.onPrimaryAdded(addedBodyField);
        addedBodyField.getBodyControl().updateState(Button.State.PRESSED);
        onAddLayerButtonCLicked(addedBodyField);
    }

    public LayerField onAddLayerButtonCLicked(BodyField bodyField) {
        LayerModel layerModel = userInterface.getToolModel().createNewLayer(bodyField.getPrimaryKey());
        LayerField addedLayerField = window.addLayerField(bodyField.getPrimaryKey(), layerModel.getLayerId());
        addedLayerField.setText(layerModel.getModelName());
        PointsShape pointsShape = new PointsShape(userInterface);
        userInterface.addElement(pointsShape);
        layerModel.setPointsShape(pointsShape);
        layerModel.setField(addedLayerField);
        resetUpDownArrows(bodyField.getPrimaryKey());
        addedLayerField.getLayerControl().updateState(Button.State.PRESSED);
        onSecondaryAdded(addedLayerField);
        bodyField.getBodyControl().updateState(Button.State.PRESSED);
        onPrimaryButtonClicked(bodyField);
        return addedLayerField;
    }

    public void onUpArrowButtonClicked(LayerField layerField) {
        int primaryKey = layerField.getPrimaryKey();
        int secondaryKey = layerField.getSecondaryKey();
        int index1 = window.getLayout().getSectionByKey(primaryKey).getIndexOfKey(secondaryKey);
        int index2 = index1 - 1;
        window.getLayout().getSectionByKey(primaryKey).swapSecondaries(index1, index2);
        userInterface.getToolModel().swapLayers(layerField.getPrimaryKey(), index1, index2);
        resetUpDownArrows(primaryKey);
        updateLayout();
    }

    private void resetUpDownArrows(int primaryKey) {
        int numberOfSecondaries = window.getLayout().getSecondariesSize(primaryKey);
        if (numberOfSecondaries > 0)
            disableUpButton(primaryKey);
        for (int i = 1; i < numberOfSecondaries; i++)
            enableUpButton(primaryKey, i);

        int lastIndex = numberOfSecondaries - 1;
        if (lastIndex >= 0)
            disableDownButton(primaryKey, lastIndex);
        for (int i = 0; i < lastIndex; i++) enableDownButton(primaryKey, i);
    }

    private void disableUpButton(int primaryKey) {
        Button<LayerWindowController> upButton = window.getLayout().getSecondaryByIndex(primaryKey, 0).getArrowUpButton();
        upButton.updateState(Button.State.DISABLED);
    }

    private void enableUpButton(int primaryKey, int index) {
        Button<LayerWindowController> upButton = window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowUpButton();
        upButton.updateState(Button.State.NORMAL);
    }

    private void disableDownButton(int primaryKey, int index) {
        Button<LayerWindowController> upButton = window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowDownButton();
        upButton.updateState(Button.State.DISABLED);
    }

    private void enableDownButton(int primaryKey, int index) {
        Button<LayerWindowController> upButton = window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowDownButton();
        upButton.updateState(Button.State.NORMAL);
    }

    public void onDownArrowButtonClicked(LayerField layerField) {
        int primaryKey = layerField.getPrimaryKey();
        int secondaryKey = layerField.getSecondaryKey();
        int index1 = window.getLayout().getSectionByKey(primaryKey).getIndexOfKey(secondaryKey);
        int index2 = index1 + 1;
        window.getLayout().getSectionByKey(primaryKey).swapSecondaries(index1, index2);
        resetUpDownArrows(primaryKey);
        updateLayout();
    }

    private void detachLayerModelShape(LayerModel layerModel) {
        ArrayList<PointsShape> pointsShapes = layerModel.getPointsShapes();
        for (PointsShape pointsShape : pointsShapes) {
            userInterface.removeElement(pointsShape);
            pointsShape.dispose();
        }
    }


    public void onLayerRemoveButtonReleased(LayerField layerField) {
        window.getLayout().removeSecondary(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        LayerModel layerModel = userInterface.getToolModel().getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        detachLayerModelShape(layerModel);

        userInterface.getToolModel().removeLayer(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        resetUpDownArrows(layerField.getPrimaryKey());
        updateLayout();
        userInterface.getToolModel().updateMesh();
    }


    public void onBodyRemoveButtonReleased(BodyField bodyField) {
        BodyModel bodyModel = userInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
        for (LayerModel layerModel : bodyModel.getLayers()) {
            detachLayerModelShape(layerModel);
        }

        window.getLayout().removePrimary(bodyField.getPrimaryKey());
        userInterface.getToolModel().removeBody(bodyField.getPrimaryKey());
        updateLayout();
        userInterface.getJointSettingsWindowController().updateBodySelectionFields();
        userInterface.getProjectileOptionsController().updateMissileSelectionFields();
        userInterface.getItemWindowController().refresh();
        userInterface.getToolModel().updateMesh();
    }

    public void onLayerSettingsButtonReleased(LayerField layerField) {
        LayerModel layerModel = getLayerModel(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        layerSettingsWindowController.openWindow();
        unfold();
        layerSettingsWindowController.onModelUpdated(layerModel);
    }

    public void onBodySettingsButtonReleased(BodyField bodyField) {
        BodyModel bodyModel = getBodyModel(bodyField.getPrimaryKey());
        bodySettingsWindowController.openWindow();
        unfold();
        bodySettingsWindowController.onModelUpdated(bodyModel);
    }

    public DecorationField onLayerAddDecorationClicked(LayerField layerField) {
        PointsShape pointsShape = new PointsShape(userInterface);
        userInterface.addElement(pointsShape);
        DecorationModel decorationModel = userInterface.getToolModel().createNewDecoration(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        decorationModel.setPointsShape(pointsShape);
        DecorationField decorationField = window.addDecorationField(layerField.getPrimaryKey(), layerField.getSecondaryKey(), decorationModel.getDecorationId());
        decorationModel.setField(decorationField);
        decorationField.setText(decorationModel.getModelName());
        onSecondaryButtonClicked(layerField);
        layerField.getLayerControl().updateState(Button.State.PRESSED);
        super.onTertiaryAdded(decorationField);
        decorationField.getDecorationControl().updateState(Button.State.PRESSED);
        onTertiaryButtonClicked(decorationField);

        return decorationField;
    }

    public void onDecorationRemoveButtonReleased(DecorationField decorationField) {

        window.getLayout().removeTertiary(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        DecorationModel decorationModel = userInterface.getToolModel().removeDecoration(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        PointsShape pointsShape = decorationModel.getPointsShape();
        userInterface.removeElement(pointsShape);
        pointsShape.dispose();

        updateLayout();
    }


    public void onDecorationSettingsButtonReleased(DecorationField decorationField) {
        DecorationModel model = (DecorationModel) getDecorationModel(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        decorationSettingsWindowController.openWindow();
        decorationSettingsWindowController.onModelUpdated(model);
    }


    public BodyModel getBodyModel(int primaryKey) {

        return userInterface.getToolModel().getBodyModelById(primaryKey);
    }

    public LayerModel getLayerModel(int primaryKey, int secondaryKey) {
        return userInterface.getToolModel().getLayerModelById(primaryKey, secondaryKey);
    }

    public PointsModel<?> getDecorationModel(int primaryKey, int secondaryKey, int tertiaryKey) {
        return userInterface.getToolModel().getDecorationModelById(primaryKey, secondaryKey, tertiaryKey);
    }


    public void changeLayerName(String blockName, int primaryKey, int secondaryKey) {
        window.getLayout().getSecondary(primaryKey, secondaryKey).getLayerControl().setTitle(blockName);
    }

    public void changeBodyName(String bodyName, int primaryKey) {
        window.getLayout().getPrimaryByKey(primaryKey).getBodyControl().setTitle(bodyName);
    }

    public void setBodySettingsWindowController(BodySettingsWindowController bodySettingsWindowController) {
        this.bodySettingsWindowController = bodySettingsWindowController;
    }

    public void setLayerSettingsWindowController(LayerSettingsWindowController layerSettingsWindowController) {
        this.layerSettingsWindowController = layerSettingsWindowController;
    }

    public void setDecorationSettingsWindowController(DecorationSettingsWindowController decorationSettingsWindowController) {
        this.decorationSettingsWindowController = decorationSettingsWindowController;
    }

    public PointsModel<?> getSelectedPointsModel() {
        return this.selectedPointsModel;
    }


    public List<PointImage> getModelMovables() {
        if (getSelectedPointsModel() != null) {
            return getSelectedPointsModel().getPointsShape().getMovablePointImages();
        }
        return null;
    }

}
