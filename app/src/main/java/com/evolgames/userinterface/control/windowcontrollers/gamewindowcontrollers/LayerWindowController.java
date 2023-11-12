package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.windowcontrollers.TwoLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.Strings;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.gamewindows.LayersWindow;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField;
import java.util.ArrayList;
import java.util.List;

public class LayerWindowController
    extends TwoLevelSectionedAdvancedWindowController<
        LayersWindow, BodyField, LayerField, DecorationField> {

  private OutlineController outlineController;
  private EditorUserInterface editorUserInterface;
  private BodySettingsWindowController bodySettingsWindowController;
  private LayerSettingsWindowController layerSettingsWindowController;
  private DecorationSettingsWindowController decorationSettingsWindowController;
  private PointsModel<?> selectedPointsModel;

  public void setOutlineController(OutlineController outlineController) {
    this.outlineController = outlineController;
  }

  public void setUserInterface(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  @Override
  public void init() {
    if (editorUserInterface.getToolModel() == null) {
      return;
    }
    ArrayList<BodyModel> bodies = editorUserInterface.getToolModel().getBodies();
    for (int i = 0; i < bodies.size(); i++) {
      BodyModel bodyModel = bodies.get(i);
      BodyField bodyField = window.addBodyField(bodyModel.getBodyId(), i == 0);
      bodyField.setText(bodyModel.getModelName());
      bodyModel.setField(bodyField);
      ArrayList<LayerModel> layers = bodyModel.getLayers();
      for (int j = 0; j < layers.size(); j++) {
        LayerModel layerModel = layers.get(j);
        LayerField layerField =
            window.addLayerField(bodyModel.getBodyId(), layerModel.getLayerId());
        layerModel.setField(layerField);
        layerField.setText(layerModel.getModelName());

        for (DecorationModel decorationModel : layerModel.getDecorations()) {
          DecorationField decorationField =
              window.addDecorationField(
                  bodyModel.getBodyId(),
                  layerModel.getLayerId(),
                  decorationModel.getDecorationId());
          decorationModel.setField(decorationField);
          decorationField.setText(decorationModel.getModelName());
        }
      }
    }

    for (BodyModel model : editorUserInterface.getToolModel().getBodies()) {
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
          for (int j = 0;
              j < window.getLayout().getSecondariesSize(otherBodyField.getPrimaryKey());
              j++) {
            LayerField otherLayerField =
                window.getLayout().getSecondaryByIndex(otherBodyField.getPrimaryKey(), j);
            otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(otherLayerField);
          }
        }
    }
    BodyModel bodyModel =
        editorUserInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
    LayerModel selectedLayerModel = null;
    for (int j = 0; j < window.getLayout().getSecondariesSize(bodyField.getPrimaryKey()); j++) {
      LayerField layerField = window.getLayout().getSecondaryByIndex(bodyField.getPrimaryKey(), j);
      if (layerField.getLayerControl().getState() == Button.State.PRESSED) {
        selectedLayerModel =
            editorUserInterface
                .getToolModel()
                .getLayerModelById(bodyField.getPrimaryKey(), layerField.getSecondaryKey());
      }
    }
    DecorationModel selectedDecorationModel = null;
    if (selectedLayerModel != null) {
      for (int j = 0;
          j
              < window
                  .getLayout()
                  .getTertiariesSize(
                      selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId());
          j++) {
        DecorationField decorationField =
            window
                .getLayout()
                .getTertiaryByIndex(
                    selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId(), j);
        if (decorationField.getDecorationControl().getState() == Button.State.PRESSED) {
          selectedDecorationModel =
              editorUserInterface
                  .getToolModel()
                  .getDecorationModelById(
                      decorationField.getPrimaryKey(),
                      decorationField.getSecondaryKey(),
                      decorationField.getTertiaryKey());
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
      LayerField otherLayerField =
          window.getLayout().getSecondaryByIndex(layerField.getPrimaryKey(), i);
      if (otherLayerField != layerField) {
        otherLayerField.getLayerControl().updateState(Button.State.NORMAL);
        onSecondaryButtonReleased(otherLayerField);
      }
    }
    layerField.getLayerControl().click();
    BodyModel selectedBodyModel =
        editorUserInterface.getToolModel().getBodyModelById(layerField.getPrimaryKey());
    LayerModel selectedLayerModel =
        editorUserInterface
            .getToolModel()
            .getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
    DecorationModel selectedDecorationModel = null;
    for (int j = 0;
        j
            < window
                .getLayout()
                .getTertiariesSize(selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId());
        j++) {
      DecorationField decorationField =
          window
              .getLayout()
              .getTertiaryByIndex(
                  selectedLayerModel.getBodyId(), selectedLayerModel.getLayerId(), j);
      if (decorationField.getDecorationControl().getState() == Button.State.PRESSED) {
        selectedDecorationModel =
            editorUserInterface
                .getToolModel()
                .getDecorationModelById(
                    decorationField.getPrimaryKey(),
                    decorationField.getSecondaryKey(),
                    decorationField.getTertiaryKey());
      }
    }
    if (selectedDecorationModel != null) {
      this.selectedPointsModel = selectedDecorationModel;
    } else {
      this.selectedPointsModel = selectedLayerModel;
    }

    layerSettingsWindowController.onModelUpdated(selectedLayerModel);
    outlineController.onSelectionUpdated(
        selectedBodyModel, selectedLayerModel, selectedDecorationModel);
  }

  @Override
  public void onSecondaryButtonReleased(LayerField layerField) {
    super.onSecondaryButtonReleased(layerField);
    BodyModel selectedBodyModel =
        editorUserInterface.getToolModel().getBodyModelById(layerField.getPrimaryKey());
    layerField.getLayerControl().release();
    selectedPointsModel = null;
    outlineController.onSelectionUpdated(selectedBodyModel, null, null);
  }

  @Override
  public void onTertiaryButtonClicked(DecorationField decorationField) {
    super.onTertiaryButtonClicked(decorationField);

    BodyModel selectedBodyModel =
        editorUserInterface.getToolModel().getBodyModelById(decorationField.getPrimaryKey());
    LayerModel selectedLayerModel =
        editorUserInterface
            .getToolModel()
            .getLayerModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey());
    DecorationModel selectedDecorationModel =
        editorUserInterface
            .getToolModel()
            .getDecorationModelById(
                decorationField.getPrimaryKey(),
                decorationField.getSecondaryKey(),
                decorationField.getTertiaryKey());
    for (int i = 0;
        i
            < window
                .getLayout()
                .getTertiariesSize(
                    decorationField.getPrimaryKey(), decorationField.getSecondaryKey());
        i++) {
      DecorationField otherDecorationField =
          window
              .getLayout()
              .getTertiaryByIndex(
                  decorationField.getPrimaryKey(), decorationField.getSecondaryKey(), i);
      if (otherDecorationField != decorationField) {
        otherDecorationField.getDecorationControl().updateState(Button.State.NORMAL);
        onTertiaryButtonReleased(otherDecorationField);
      }
    }
    this.selectedPointsModel = selectedDecorationModel;
    outlineController.onSelectionUpdated(
        selectedBodyModel, selectedLayerModel, selectedDecorationModel);
  }

  @Override
  public void onTertiaryButtonReleased(DecorationField decorationField) {
    super.onTertiaryButtonReleased(decorationField);
    BodyModel selectedBodyModel =
        editorUserInterface.getToolModel().getBodyModelById(decorationField.getPrimaryKey());
    LayerModel selectedLayerModel =
        editorUserInterface
            .getToolModel()
            .getLayerModelById(decorationField.getPrimaryKey(), decorationField.getSecondaryKey());
    this.selectedPointsModel = null;
    outlineController.onSelectionUpdated(selectedBodyModel, selectedLayerModel, null);
  }

  public void onAddBodyButtonClicked() {
    BodyModel bodyModel = editorUserInterface.getToolModel().createNewBody();
    BodyField addedBodyField = window.addBodyField(bodyModel.getBodyId(), false);
    bodyModel.setField(addedBodyField);
    addedBodyField.setText(bodyModel.getModelName());
    ItemWindowController itemWindowController = editorUserInterface.getItemWindowController();
    itemWindowController.onBodyCreated(bodyModel);
    editorUserInterface.getJointSettingsWindowController().updateBodySelectionFields();
    this.onPrimaryAdded(addedBodyField);
    addedBodyField.getBodyControl().updateState(Button.State.PRESSED);
    onAddLayerButtonCLicked(addedBodyField);
  }

  public LayerField onAddLayerButtonCLicked(BodyField bodyField) {
    LayerModel layerModel =
        editorUserInterface.getToolModel().createNewLayer(bodyField.getPrimaryKey());
    LayerField addedLayerField =
        window.addLayerField(bodyField.getPrimaryKey(), layerModel.getLayerId());
    addedLayerField.setText(layerModel.getModelName());
    PointsShape pointsShape = new PointsShape(editorUserInterface);
    editorUserInterface.addElement(pointsShape);
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
    editorUserInterface.getToolModel().swapLayers(layerField.getPrimaryKey(), index1, index2);
    resetUpDownArrows(primaryKey);
    updateLayout();
  }

  private void resetUpDownArrows(int primaryKey) {
    int numberOfSecondaries = window.getLayout().getSecondariesSize(primaryKey);
    if (numberOfSecondaries > 0) disableUpButton(primaryKey);
    for (int i = 1; i < numberOfSecondaries; i++) enableUpButton(primaryKey, i);

    int lastIndex = numberOfSecondaries - 1;
    if (lastIndex >= 0) disableDownButton(primaryKey, lastIndex);
    for (int i = 0; i < lastIndex; i++) enableDownButton(primaryKey, i);
  }

  private void disableUpButton(int primaryKey) {
    Button<LayerWindowController> upButton =
        window.getLayout().getSecondaryByIndex(primaryKey, 0).getArrowUpButton();
    upButton.updateState(Button.State.DISABLED);
  }

  private void enableUpButton(int primaryKey, int index) {
    Button<LayerWindowController> upButton =
        window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowUpButton();
    upButton.updateState(Button.State.NORMAL);
  }

  private void disableDownButton(int primaryKey, int index) {
    Button<LayerWindowController> upButton =
        window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowDownButton();
    upButton.updateState(Button.State.DISABLED);
  }

  private void enableDownButton(int primaryKey, int index) {
    Button<LayerWindowController> upButton =
        window.getLayout().getSecondaryByIndex(primaryKey, index).getArrowDownButton();
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
      editorUserInterface.removeElement(pointsShape);
      pointsShape.dispose();
    }
  }

  public void onLayerRemoveButtonReleased(LayerField layerField) {
    LayerModel layerModel =
        editorUserInterface
            .getToolModel()
            .getLayerModelById(layerField.getPrimaryKey(), layerField.getSecondaryKey());
    editorUserInterface.doWithConfirm(
        String.format(
            Strings.LAYER_DELETE_CONFIRM,
            layerModel.getModelName(),
            layerModel.getBodyModel().getModelName()),
        () -> {
          window
              .getLayout()
              .removeSecondary(layerField.getPrimaryKey(), layerField.getSecondaryKey());
          detachLayerModelShape(layerModel);
          editorUserInterface
              .getToolModel()
              .removeLayer(layerField.getPrimaryKey(), layerField.getSecondaryKey());
          resetUpDownArrows(layerField.getPrimaryKey());
          updateLayout();
          editorUserInterface.getToolModel().updateMesh();
        });
  }

  public void onBodyRemoveButtonReleased(BodyField bodyField) {
    BodyModel bodyModel =
        editorUserInterface.getToolModel().getBodyModelById(bodyField.getPrimaryKey());
    editorUserInterface.doWithConfirm(
        String.format(Strings.BODY_DELETE_CONFIRM, bodyModel.getModelName()),
        () -> {
          for (LayerModel layerModel : bodyModel.getLayers()) {
            detachLayerModelShape(layerModel);
          }
          window.getLayout().removePrimary(bodyField.getPrimaryKey());
          editorUserInterface.getToolModel().removeBody(bodyField.getPrimaryKey());
          updateLayout();
          editorUserInterface.getJointSettingsWindowController().updateBodySelectionFields();
          editorUserInterface.getProjectileOptionsController().updateMissileSelectionFields();
          editorUserInterface.getItemWindowController().refresh();
          editorUserInterface.getToolModel().updateMesh();
        });
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
    PointsShape pointsShape = new PointsShape(editorUserInterface);
    editorUserInterface.addElement(pointsShape);
    DecorationModel decorationModel =
        editorUserInterface
            .getToolModel()
            .createNewDecoration(layerField.getPrimaryKey(), layerField.getSecondaryKey());
    decorationModel.setPointsShape(pointsShape);
    DecorationField decorationField =
        window.addDecorationField(
            layerField.getPrimaryKey(),
            layerField.getSecondaryKey(),
            decorationModel.getDecorationId());
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
    DecorationModel decorationModel =
        editorUserInterface
            .getToolModel()
            .removeDecoration(
                decorationField.getPrimaryKey(),
                decorationField.getSecondaryKey(),
                decorationField.getTertiaryKey());
    editorUserInterface.doWithConfirm(
        String.format(
            Strings.DECORATION_DELETE_CONFIRM,
            decorationModel.getModelName(),
            decorationModel.getLayerModel().getModelName()),
        () -> {
          window
              .getLayout()
              .removeTertiary(
                  decorationField.getPrimaryKey(),
                  decorationField.getSecondaryKey(),
                  decorationField.getTertiaryKey());
          PointsShape pointsShape = decorationModel.getPointsShape();
          editorUserInterface.removeElement(pointsShape);
          pointsShape.dispose();
          updateLayout();
        });
  }

  public void onDecorationSettingsButtonReleased(DecorationField decorationField) {
    DecorationModel model =
        (DecorationModel)
            getDecorationModel(
                decorationField.getPrimaryKey(),
                decorationField.getSecondaryKey(),
                decorationField.getTertiaryKey());
    decorationSettingsWindowController.openWindow();
    decorationSettingsWindowController.onModelUpdated(model);
  }

  public BodyModel getBodyModel(int primaryKey) {

    return editorUserInterface.getToolModel().getBodyModelById(primaryKey);
  }

  public LayerModel getLayerModel(int primaryKey, int secondaryKey) {
    return editorUserInterface.getToolModel().getLayerModelById(primaryKey, secondaryKey);
  }

  public PointsModel<?> getDecorationModel(int primaryKey, int secondaryKey, int tertiaryKey) {
    return editorUserInterface
        .getToolModel()
        .getDecorationModelById(primaryKey, secondaryKey, tertiaryKey);
  }

  public void changeLayerName(String blockName, int primaryKey, int secondaryKey) {
    window.getLayout().getSecondary(primaryKey, secondaryKey).getLayerControl().setTitle(blockName);
  }

  public void changeBodyName(String bodyName, int primaryKey) {
    window.getLayout().getPrimaryByKey(primaryKey).getBodyControl().setTitle(bodyName);
  }

  public void setBodySettingsWindowController(
      BodySettingsWindowController bodySettingsWindowController) {
    this.bodySettingsWindowController = bodySettingsWindowController;
  }

  public void setLayerSettingsWindowController(
      LayerSettingsWindowController layerSettingsWindowController) {
    this.layerSettingsWindowController = layerSettingsWindowController;
  }

  public void setDecorationSettingsWindowController(
      DecorationSettingsWindowController decorationSettingsWindowController) {
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
