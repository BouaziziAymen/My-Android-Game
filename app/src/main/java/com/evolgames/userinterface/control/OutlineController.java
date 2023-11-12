package com.evolgames.userinterface.control;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.Screen;
import com.evolgames.userinterface.view.shapes.PointsShape;

public class OutlineController extends Controller {
  private EditorUserInterface editorUserInterface;
  private ProperModel<?> lastSelectedItem;
  private BodyModel selectedBodyModel;
  private LayerModel selectedLayerModel;
  private DecorationModel selectedDecorationModel;

  @Override
  public void init() {}

  private void resetAllItems() {
    for (BodyModel bodyModel : editorUserInterface.getToolModel().getBodies()) {
      for (ProjectileModel projectileModel : bodyModel.getProjectileModels()) {
        projectileModel.getProjectileShape().release();
        if (projectileModel.getProjectileField() != null) {
          projectileModel.getProjectileField().hideFields();
        }
      }
      for (CasingModel casingModel : bodyModel.getCasingModels()) {
        casingModel.getCasingShape().release();
        if (casingModel.getCasingField() != null) {
          casingModel.getCasingField().hideFields();
        }
      }
      for (BombModel bombModel : bodyModel.getBombModels()) {
        bombModel.getBombShape().release();
        if (bombModel.getBombField() != null) {
          bombModel.getBombField().hideFields();
        }
      }
    }
  }

  private void resetAll() {
    for (BodyModel bodyModel : editorUserInterface.getToolModel().getBodies()) {
      if (bodyModel.getField() != null) {
        bodyModel.getField().hideFields();
      }
      for (LayerModel layerModel : bodyModel.getLayers()) {
        PointsShape layerShape = layerModel.getPointsShape();
        layerShape.setLineLoopColor(Colors.white);
        layerShape.setVisible(false);
        if (layerModel.getField() != null) {
          layerModel.getField().hideFields();
        }
        for (DecorationModel decorationModel : layerModel.getDecorations()) {
          PointsShape decorationShape = decorationModel.getPointsShape();
          decorationShape.setLineLoopColor(Colors.white);
          decorationShape.setVisible(false);
          if (decorationModel.getField() != null) {
            decorationModel.getField().hideFields();
          }
        }
      }
    }
    resetAllItems();
  }

  public void onItemSelectionUpdated(ProperModel<?> selectedItemModel) {
    resetAllItems();
    lastSelectedItem = selectedItemModel;
    if (selectedItemModel == null) {
      return;
    }
    if (selectedItemModel instanceof ProjectileModel) {
      ProjectileModel projectileModel = (ProjectileModel) selectedItemModel;
      projectileModel.getProjectileShape().select();
      projectileModel.getProjectileField().showFields();
    } else if (selectedItemModel instanceof CasingModel) {
      CasingModel casingModel = (CasingModel) selectedItemModel;
      casingModel.getCasingShape().select();
      casingModel.getCasingField().showFields();
    } else if (selectedItemModel instanceof BombModel) {
      BombModel bombModel = (BombModel) selectedItemModel;
      bombModel.getBombShape().select();
      bombModel.getBombField().showFields();
    }
  }

  public void onSelectionUpdated(
      BodyModel selectedBodyModel,
      LayerModel selectedLayerModel,
      DecorationModel selectedDecorationModel) {
    this.resetAll();
    this.selectedBodyModel = selectedBodyModel;
    this.selectedLayerModel = selectedLayerModel;
    this.selectedDecorationModel = selectedDecorationModel;
    if (selectedDecorationModel != null) {
      selectBodyModel(selectedBodyModel, Colors.white);
      selectedDecorationModel.getPointsShape().setPointsVisible(true);
      selectedDecorationModel.getPointsShape().setOutlineVisible(true);
      selectedDecorationModel.getPointsShape().setLineLoopColor(Colors.palette1_green);
      selectedLayerModel.getPointsShape().setLineLoopColor(Colors.palette1_blue);
      selectedLayerModel.getPointsShape().setOutlineVisible(true);
    } else if (selectedLayerModel != null) {
      selectBodyModel(selectedBodyModel, Colors.white);
      selectedLayerModel.getPointsShape().setLineLoopColor(Colors.palette1_green);
      selectedLayerModel.getPointsShape().setOutlineVisible(true);
      selectedLayerModel.getPointsShape().setPointsVisible(true);
    } else if (selectedBodyModel != null) {
      this.selectBodyModel(selectedBodyModel, Colors.white);
    } else {
      this.deselectBodyModels();
    }
    if (selectedBodyModel != null) {
      selectedBodyModel.getField().showFields();
    }
    if (selectedLayerModel != null) {
      selectedLayerModel.getField().showFields();
    }
    if (selectedDecorationModel != null) {
      selectedDecorationModel.getField().showFields();
    }
  }

  private void deselectBodyModels() {
    for (BodyModel bodyModel : editorUserInterface.getToolModel().getBodies()) {
      bodyModel
          .getLayers()
          .forEach(
              layerModel -> {
                layerModel.getPointsShape().setVisible(false);
                layerModel.getPointsShape().setLineLoopColor(Colors.white);
              });
    }
  }

  private void selectBodyModel(BodyModel bodyModel, Color color) {
    bodyModel
        .getLayers()
        .forEach(
            layerModel -> {
              layerModel.getPointsShape().setOutlineVisible(true);
              layerModel.getPointsShape().setLineLoopColor(color);
            });
  }

  public void setUserInterface(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  public void onScreenChanged(Screen selectedScreen) {
    resetAll();
    switch (selectedScreen) {
      case DRAW_SCREEN:
        this.onSelectionUpdated(selectedBodyModel, selectedLayerModel, selectedDecorationModel);
        break;
      case JOINTS_SCREEN:
        JointSettingsWindowController jointSettingsWindowController =
            editorUserInterface.getJointSettingsWindowController();
        this.onJointBodySelectionUpdated(
            jointSettingsWindowController.getBodyModelA(),
            jointSettingsWindowController.getBodyModelB());
        break;
      case ITEMS_SCREEN:
        ItemWindowController itemWindowController = editorUserInterface.getItemWindowController();
        this.onSelectionUpdated(itemWindowController.getSelectedBodyModel(), null, null);
        for (BodyModel bodyModel : editorUserInterface.getToolModel().getBodies()) {
          for (ProjectileModel projectileModel : bodyModel.getProjectileModels()) {
            projectileModel.getProjectileShape().setVisible(true);
          }
          for (CasingModel casingModel : bodyModel.getCasingModels()) {
            casingModel.getCasingShape().setVisible(true);
          }
          for (BombModel bombModel : bodyModel.getBombModels()) {
            bombModel.getBombShape().setVisible(true);
          }
        }
        this.onItemSelectionUpdated(this.lastSelectedItem);
        break;
      case IMAGE_SCREEN:
        break;
      case SAVE_SCREEN:
        break;
      case NONE:
        break;
    }
  }

  public void onJointBodySelectionUpdated(BodyModel bodyModelA, BodyModel bodyModelB) {
    resetAll();
    if (bodyModelA != null) {
      selectBodyModel(bodyModelA, Colors.palette1_joint_a_color);
    }
    if (bodyModelB != null) {
      selectBodyModel(bodyModelB, Colors.palette1_joint_b_color);
    }
  }
}
