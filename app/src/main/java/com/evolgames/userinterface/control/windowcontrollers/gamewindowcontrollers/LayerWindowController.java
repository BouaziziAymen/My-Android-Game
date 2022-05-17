package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.windowcontrollers.TwoLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationPointsModel;
import com.evolgames.userinterface.model.LayerPointsModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.BodyOutlineShape;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.windows.gamewindows.LayersWindow;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField1;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField1;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField1;

import java.util.ArrayList;

public class LayerWindowController extends TwoLevelSectionedAdvancedWindowController<LayersWindow, BodyField1, LayerField1, DecorationField1> {

    private UserInterface userInterface;
    private BodyField1 selectedBodyField;
    private LayerField1 selectedLayerField;
    private DecorationField1 selectedDecorationField;
    private BodySettingsWindowController bodySettingsWindowController;
    private LayerSettingsWindowController layerSettingsWindowController;
    private DecorationSettingsWindowController decorationSettingsWindowController;


    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void init() {
        if (userInterface.getToolModel() == null) return;
        ArrayList<BodyModel> bodies = userInterface.getToolModel().getBodies();
        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), i == 0);
            ArrayList<LayerPointsModel> layers = bodyModel.getLayers();
            for (int j = 0; j < layers.size(); j++) {
                LayerPointsModel layerModel = layers.get(j);
                window.addLayerField(layerModel.getModelName(), bodyModel.getBodyId(), layerModel.getLayerId());


                for (DecorationPointsModel decorationModel : layerModel.getDecorations()) {
                    window.addDecorationField(bodyModel.getBodyId(), layerModel.getLayerId(), decorationModel.getDecorationId());
                }
            }
        }
        BodyModel selectedBody = getSelectedBodyModel();
        if (selectedBody != null) {
            LayerPointsModel selectedLayer = getSelectedLayer();

            BodyField1 firstBodyButton = window.getLayout().getSectionByKey(selectedBody.getBodyId()).getPrimary();
            onPrimaryButtonClicked(firstBodyButton);
            firstBodyButton.getBodyControl().updateState(Button.State.PRESSED);
            if (selectedLayer != null) {
                LayerField1 firstLayerButton = window.getLayout().getSecondary(selectedBody.getBodyId(), selectedLayer.getLayerId());
                onSecondaryButtonClicked(firstLayerButton);
                firstLayerButton.getLayerControl().updateState(Button.State.PRESSED);
            }

        }
        for (BodyModel model : userInterface.getToolModel().getBodies()) {
            resetUpDownArrows(model.getBodyId());
        }


        updateLayout();

    }

    public LayerPointsModel getSelectedLayer() {
        if (selectedLayerField == null) return null;
        return userInterface.getToolModel().getLayerModelById(selectedLayerField.getPrimaryKey(), selectedLayerField.getSecondaryKey());
    }

    public BodyModel getSelectedBodyModel() {
        if (selectedBodyField == null) return null;
        return userInterface.getToolModel().getBodyModelById(selectedBodyField.getPrimaryKey());
    }


    @Override
    public void onPrimaryButtonClicked(BodyField1 bodyField) {
        super.onPrimaryButtonClicked(bodyField);
        selectedBodyField = bodyField;
        if (!getSelectedBodyModel().hasSelectedLayer()) {
            getSelectedBodyModel().select();
        }

        for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
            BodyField1 otherBodyField = window.getLayout().getPrimaryByIndex(i);
            if (otherBodyField != null)
                if (otherBodyField != bodyField) {
                    otherBodyField.getBodyControl().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(otherBodyField);
                    for (int j = 0; j < window.getLayout().getSecondariesSize(otherBodyField.getPrimaryKey()); j++) {
                        LayerField1 otherLayerField = window.getLayout().getSecondaryByIndex(otherBodyField.getPrimaryKey(), j);
                        otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
                        onSecondaryButtonReleased(otherLayerField);
                    }
                }
        }
    }

    @Override
    public void onPrimaryButtonReleased(BodyField1 bodyField) {
        super.onPrimaryButtonReleased(bodyField);
        getBodyModel(bodyField.getPrimaryKey()).deselect();
        if (selectedBodyField == bodyField) selectedBodyField = null;
    }

    @Override
    public void onSecondaryButtonClicked(LayerField1 layerField) {
        super.onSecondaryButtonClicked(layerField);
        selectedLayerField = layerField;

        for (int i = 0; i < window.getLayout().getSecondariesSize(layerField.getPrimaryKey()); i++) {
            LayerField1 otherLayerField = window.getLayout().getSecondaryByIndex(layerField.getPrimaryKey(), i);
            if (otherLayerField != layerField) {
                otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
                onSecondaryButtonReleased(otherLayerField);
            }
        }
        layerField.getLayerControl().select();
        getSelectedLayerModel().select();
        getSelectedLayerModel().getBodyModel().deselect();
        ;

        layerSettingsWindowController.onModelUpdated(getSelectedLayerModel());

    }

    @Override
    public void onSecondaryButtonReleased(LayerField1 layerField) {
        super.onSecondaryButtonReleased(layerField);
        LayerPointsModel releasedModel = userInterface.getToolModel().getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        releasedModel.deselect();
        if(getSelectedBodyModel() != null)
        if(getSelectedBodyModel().getBodyId()==layerField.getPrimaryKey()){
            getSelectedBodyModel().select();
        }

        if (selectedLayerField == layerField) {
            getSelectedLayerModel().deselect();
            selectedLayerField = null;
        }
        layerField.getLayerControl().deselect();
    }

    @Override
    public void onTertiaryButtonClicked(DecorationField1 decorationField) {
        this.selectedDecorationField = decorationField;
        for (int i = 0; i < window.getLayout().getTertiariesSize(decorationField.getPrimaryKey(), decorationField.getSecondaryKey()); i++) {
            DecorationField1 otherDecorationField = window.getLayout().getTertiaryByIndex(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), i);
            if (otherDecorationField != decorationField) {
                otherDecorationField.getDecorationControl().updateState(Button.State.NORMAL);
                onTertiaryButtonReleased(otherDecorationField);
            }
        }
    }

    @Override
    public void onTertiaryButtonReleased(DecorationField1 tertiary) {
        super.onTertiaryButtonReleased(tertiary);

        if (selectedDecorationField == tertiary) {
            DecorationPointsModel decorationModel = userInterface.getToolModel().getDecorationModelById(tertiary.getPrimaryKey(), tertiary.getSecondaryKey(), tertiary.getTertiaryKey());
            decorationModel.deselect();
            PointsModel selected = getSelectedShape();
            if (selected != null) selected.select();
            selectedDecorationField = null;
        }
    }

    public void onAddBodyButtonClicked() {
        BodyModel bodyModel = userInterface.getToolModel().createNewBody();
        selectedBodyField = window.addBodyField(bodyModel.getModelName(), bodyModel.getBodyId(), false);
        BodyOutlineShape bodyOutlineShape = new BodyOutlineShape(userInterface, bodyModel);
        bodyModel.setBodyOutlineShape(bodyOutlineShape);
        ItemWindowController itemWindowController = userInterface.getItemWindowController();
        itemWindowController.onBodyCreated(bodyModel);

        userInterface.getJointSettingsWindowController().updateBodySelectionFields();
        super.onPrimaryAdded(selectedBodyField);
        selectedBodyField.getBodyControl().updateState(Button.State.PRESSED);
        onAddLayerButtonCLicked(selectedBodyField);
    }

    public LayerField1 onAddLayerButtonCLicked(BodyField1 bodyField) {
        LayerPointsModel layerModel = userInterface.getToolModel().createNewLayer(bodyField.getPrimaryKey());
        selectedLayerField = window.addLayerField(layerModel.getModelName(), bodyField.getPrimaryKey(), layerModel.getLayerId());
        PointsShape pointsShape = new PointsShape(userInterface);
        userInterface.addElement(pointsShape);
        layerModel.setPointsShape(pointsShape);
        pointsShape.select();
        resetUpDownArrows(bodyField.getPrimaryKey());
        selectedLayerField.getLayerControl().updateState(Button.State.PRESSED);
        bodyField.getBodyControl().updateState(Button.State.PRESSED);
        onPrimaryButtonClicked(bodyField);
        super.onSecondaryAdded(selectedLayerField);
        return selectedLayerField;
    }

    public void onUpArrowButtonClicked(LayerField1 layerField) {
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

    public void onDownArrowButtonClicked(LayerField1 layerField) {
        int primaryKey = layerField.getPrimaryKey();
        int secondaryKey = layerField.getSecondaryKey();
        int index1 = window.getLayout().getSectionByKey(primaryKey).getIndexOfKey(secondaryKey);
        int index2 = index1 + 1;
        window.getLayout().getSectionByKey(primaryKey).swapSecondaries(index1, index2);
        resetUpDownArrows(primaryKey);
        updateLayout();
    }

    public void onLayerRemoveButtonClicked(LayerField1 layerField) {
    }

    private void detachLayerModelShape(LayerPointsModel layerModel) {
        ArrayList<PointsShape> pointsShapes = layerModel.getPointsShapes();
        for (PointsShape pointsShape : pointsShapes) {
            userInterface.removeElement(pointsShape);
            pointsShape.dispose();
        }
    }


    public void onLayerRemoveButtonReleased(LayerField1 layerField) {
        if (selectedLayerField == layerField) selectedLayerField = null;
        window.getLayout().removeSecondary(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        LayerPointsModel layerModel = userInterface.getToolModel().getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        detachLayerModelShape(layerModel);

        userInterface.getToolModel().removeLayer(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        resetUpDownArrows(layerField.getPrimaryKey());
        updateLayout();
        userInterface.getToolModel().updateMesh();
    }

    public void onBodyRemoveButtonClicked(BodyField1 bodyField) {

    }

    public void onBodyRemoveButtonReleased(BodyField1 bodyField) {
        if (selectedBodyField == bodyField) selectedBodyField = null;
        BodyModel bodyModel = userInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
        for (LayerPointsModel layerModel : bodyModel.getLayers()) {
            detachLayerModelShape(layerModel);
        }

        window.getLayout().removePrimary(bodyField.getPrimaryKey());
        userInterface.getToolModel().removeBody(bodyField.getPrimaryKey());
        updateLayout();
        userInterface.getJointSettingsWindowController().updateBodySelectionFields();
        userInterface.getProjectileOptionsController().updateBodySelectionFields();
        userInterface.getItemWindowController().refresh();
        userInterface.getToolModel().updateMesh();

    }

    public void onLayerSettingsButtonClicked(LayerField1 layerField) {
    }

    public void onLayerSettingsButtonReleased(LayerField1 layerField) {
        LayerPointsModel layerModel = getLayerModel(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        layerSettingsWindowController.openWindow();
        disableFoldButton();
        disableCloseButton();
        layerSettingsWindowController.onModelUpdated(layerModel);
    }

    public void onBodySettingsButtonReleased(BodyField1 bodyField) {
        BodyModel bodyModel = getBodyModel(bodyField.getPrimaryKey());
        bodySettingsWindowController.openWindow();
        disableFoldButton();
        disableCloseButton();
        bodySettingsWindowController.onModelUpdated(bodyModel);
    }

    public DecorationField1 onLayerAddDecorationClicked(LayerField1 layerField) {

        DecorationField1 decorationField = window.addDecorationField(layerField.getPrimaryKey(), layerField.getSecondaryKey(), userInterface.getToolModel().getNewDecorationId(layerField.getPrimaryKey(), layerField.getSecondaryKey()));
        PointsShape pointsShape = new PointsShape(userInterface);
        userInterface.addElement(pointsShape);
        DecorationPointsModel decorationModel = userInterface.getToolModel().createNewDecoration(layerField.getPrimaryKey(), layerField.getSecondaryKey());
        decorationModel.setPointsShape(pointsShape);


        onSecondaryButtonClicked(layerField);
        layerField.getLayerControl().updateState(Button.State.PRESSED);
        super.onTertiaryAdded(decorationField);
        decorationField.getDecorationControl().updateState(Button.State.PRESSED);
        onTertiaryButtonClicked(decorationField);

        return decorationField;
    }

    public void onLayerAddDecorationReleased(LayerField1 layerField) {


    }

    public void onDecorationRemoveButtonClicked(DecorationField1 decorationField) {

    }

    public void onDecorationRemoveButtonReleased(DecorationField1 decorationField) {

        window.getLayout().removeTertiary(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        DecorationPointsModel decorationModel = userInterface.getToolModel().removeDecoration(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        PointsShape pointsShape = decorationModel.getPointsShape();
        userInterface.removeElement(pointsShape);
        pointsShape.dispose();

        updateLayout();
    }

    public void onDecorationSettingsButtonClicked(DecorationField1 decorationField) {


    }

    public void onDecorationSettingsButtonReleased(DecorationField1 decorationField) {
        DecorationPointsModel model = (DecorationPointsModel) getDecorationModel(decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), decorationField.getTertiaryKey());
        decorationSettingsWindowController.openWindow();
        decorationSettingsWindowController.onModelUpdated(model);
    }

    private LayerPointsModel getSelectedLayerModel() {
        return userInterface.getToolModel().getLayerModelById(selectedLayerField.getPrimaryKey(), selectedLayerField.getSecondaryKey());
    }

    public BodyModel getBodyModel(int primaryKey) {

        return userInterface.getToolModel().getBodyModelById(primaryKey);
    }

    public LayerPointsModel getLayerModel(int primaryKey, int secondaryKey) {
        return userInterface.getToolModel().getLayerModelById(primaryKey, secondaryKey);
    }

    public PointsModel getDecorationModel(int primaryKey, int secondaryKey, int tertiaryKey) {
        return userInterface.getToolModel().getDecorationModelById(primaryKey, secondaryKey, tertiaryKey);
    }

    public LayerField1 getSelectedLayerField() {
        return selectedLayerField;
    }

    public BodyField1 getSelectedBodyField() {
        return selectedBodyField;
    }


    public void changeLayerName(String blockName, int primaryKey, int secondaryKey) {
        window.getLayout().getSecondary(primaryKey, secondaryKey).getLayerControl().setTitle(blockName);
    }

    public void changeBodyName(String bodyName, int primaryKey) {
        window.getLayout().getPrimaryByKey(primaryKey).getBodyControl().setTitle(bodyName);
    }

    public void onResume() {
        onFoldButtonClicked();
        enableCloseButton();
        enableFoldButton();
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

    public PointsModel getSelectedShape() {
        try {
            if (selectedDecorationField != null)
                return userInterface.getToolModel().getDecorationModelById(selectedDecorationField.getPrimaryKey(), selectedDecorationField.getSecondaryKey(), selectedDecorationField.getTertiaryKey());
            if (selectedLayerField != null)
                return userInterface.getToolModel().getLayerModelById(selectedLayerField.getPrimaryKey(), selectedLayerField.getSecondaryKey());
        } catch (NullPointerException ignored) {
        }
        return null;
    }
}
