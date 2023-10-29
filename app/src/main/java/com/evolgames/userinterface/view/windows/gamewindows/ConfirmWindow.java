package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ConfirmWindowController;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

import java.util.ArrayList;
import java.util.List;

public class ConfirmWindow extends AbstractLinearLayoutAdvancedWindow<LinearLayout> {


    private final List<Text> textList;
    private final float maxTextWidth;
    private final int fontId;


    public ConfirmWindow(float X, float Y, int fontId, ConfirmWindowController controller) {
        super(X, Y + 64, 3, 8, false, controller);
        this.maxTextWidth = 8 * 32 - 24;
        this.fontId = fontId;
        this.textList = new ArrayList<>();
        for(int i=0;i<3;i++){
            Text text = new Text("",fontId);
            this.textList.add(text);
            this.getLayout().addToLayout(text);
        }

        Panel panel = new Panel(0, -64, 5, true, true);

        panel.getCloseButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, panel.getCloseButton()) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onCancel();
            }
        });

        panel.getAcceptButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, panel.getAcceptButton()) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onConfirm();

            }
        });
        panel.setLowerBottomX(getWidth() / 2 - panel.getWidth() / 2);
        addElement(panel);
        this.setLowerBottomX(X-getWidth()/2);
        controller.init();
    }

    @Override
    protected LinearLayout createLayout() {
        return new LinearLayout(12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }

    public void updateText(String text) {
        for(Text t:textList){
            t.updateText("");
        }
        String[] words = text.split(" ");
        List<StringBuilder> lines = new ArrayList<>();
        lines.add(new StringBuilder());
        StringBuilder sb;
        int index = 0;
        while(index<words.length){
           sb = lines.get(lines.size()-1);
           String word = words[index];
            float width = ResourceManager.getInstance().getFontWidth(this.fontId, sb +word);
            if(width<maxTextWidth){
                sb.append(word);
                sb.append(" ");
                index++;
            } else {
                lines.add(new StringBuilder());
            }
        }
        for(int i=0;i<lines.size();i++) {
            this.textList.get(i).updateText(lines.get(i).toString().trim());
        }
    }
}
