package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.visitor.ContentTraverser;
import com.evolgames.userinterface.view.visitor.LimitBehavior;
import com.evolgames.userinterface.view.visitor.ShadeVisitBehavior;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public class LinearLayoutAdvancedWindowController<W extends AbstractLinearLayoutAdvancedWindow<?>> extends AdvancedWindowController<W> {



    public void onFoldButtonClicked() {
        super.onFoldButtonClicked();
        if(window.getScroller()!=null) {
            window.getScroller().setVisible(true);
            updateScroller();
        }
    }


    public void onFoldButtonReleased() {

        super.onFoldButtonReleased();
        if(window.getScroller()!=null) {
            window.getScroller().setVisible(false);
        }
    }

    public void onCloseButtonClicked() {
        super.onCloseButtonClicked();
        if(window.getScroller()!=null)
            window.getScroller().setVisible(false);
    }

    public void onLayoutChanged() {
        onVisibleZoneUpdate();
        updateScroller();
    }

    public void onScrolled(float pAdvance) {
        float height = window.getLayout().getHeight();
        window.getLayout().setLowerBottomY(pAdvance * height + window.getLayout().getY0());
        onVisibleZoneUpdate();
    }
    private ContentTraverser traverser = new ContentTraverser();
     protected void updateScroller(){
        if (window.getScroller() != null) window.getScroller().onHeightUpdated(window.getLayout().getHeight());
    }

     public void onVisibleZoneUpdate() {
        LimitBehavior behavior = new LimitBehavior();
        behavior.setInf(window.getVisibilityInf());
        behavior.setSup(window.getVisibilitySup());
        traverser.setBehavior(behavior);
        traverser.traverse(window.getLayout(),false);
    }
    public void updateLayout(){
         window.getLayout().updateLayout();
         onLayoutChanged();
    }

    @Override
    protected void onTextFieldTapped(TextField<?> pTextField) {
        super.onTextFieldTapped(pTextField);
           ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
           shadeVisitBehavior.setExcepted(pTextField);
           shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Hide);
           traverser.setBehavior(shadeVisitBehavior);
           traverser.traverse(window.getLayout(),false);
    }

    @Override
    protected void onTextFieldReleased(TextField<?> textField) {
        super.onTextFieldReleased(textField);
        ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
        shadeVisitBehavior.setExcepted(textField);
        shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Show);
        traverser.setBehavior(shadeVisitBehavior);
        traverser.traverse(window.getLayout(),false);
    }
}
