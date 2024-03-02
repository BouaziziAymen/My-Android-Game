package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import android.util.Log;

import com.evolgames.userinterface.control.behaviors.actions.ConfirmableAction;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.windows.gamewindows.ConfirmWindow;

import java.util.Objects;

public class ConfirmWindowController extends LinearLayoutAdvancedWindowController<ConfirmWindow> {

  private final EditorUserInterface editorUserInterface;
  ConfirmableAction confirmableAction;

  public ConfirmWindowController(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  @Override
  public void init() {
    this.closeWindow();
  }

  public void bindAction(ConfirmableAction confirmableAction) {
    this.confirmableAction = confirmableAction;
    try {
      window.updateText(this.confirmableAction.getPrompt());
    } catch (IllegalAccessException e) {
      Log.e("Error", e.toString());
    }
    this.openWindow();
    this.editorUserInterface.shade(window);
  }

  @Override
  public void closeWindow() {
    this.editorUserInterface.undoShade();
    super.closeWindow();
  }

  public void onCancel() {
    confirmableAction.onCancel();
    this.closeWindow();
  }

  public void onConfirm() {
    confirmableAction.onConfirm();
    this.closeWindow();
  }
}
