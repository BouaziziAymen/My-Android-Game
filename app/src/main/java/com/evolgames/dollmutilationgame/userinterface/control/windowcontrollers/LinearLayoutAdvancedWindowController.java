package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers;

import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;
import com.evolgames.dollmutilationgame.userinterface.view.visitor.ContentTraverser;
import com.evolgames.dollmutilationgame.userinterface.view.visitor.LimitBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.visitor.ShadeVisitBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public class LinearLayoutAdvancedWindowController<W extends AbstractLinearLayoutAdvancedWindow<?>>
        extends AdvancedWindowController<W> {

    private final ContentTraverser contentTraverser = new ContentTraverser();

    @Override
    protected void showWindowBody() {
        super.showWindowBody();
        updateScroller();
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

    public void updateScroller() {
        if (window.getScroller() != null) {
            window.getScroller().onLayoutHeightUpdated(window.getLayout().getHeight());
        }
    }

    @Override
    public void openWindow() {
        super.openWindow();
        this.updateScroller();
    }

    public void onVisibleZoneUpdate() {
        LimitBehavior behavior = new LimitBehavior();
        behavior.setInf(window.getVisibilityInf());
        behavior.setSup(window.getVisibilitySup());
        contentTraverser.setBehavior(behavior);
        contentTraverser.traverse(window.getLayout(), false);
    }

    public void updateLayout() {
        window.getLayout().updateLayout();
        onLayoutChanged();
    }

    @Override
    protected void onTextFieldTapped(TextField<?> pTextField) {
        super.onTextFieldTapped(pTextField);
        if (getSelectedTextField() == pTextField) {
            ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
            shadeVisitBehavior.setExcepted(pTextField);
            shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Hide);
            contentTraverser.setBehavior(shadeVisitBehavior);
            contentTraverser.traverse(window.getLayout(), false);
            if (window.getScroller() != null) {
                window.getScroller().setVisible(false);
            }
        }
    }

    @Override
    protected void onTextFieldReleased(TextField<?> textField) {
        super.onTextFieldReleased(textField);
        if (getSelectedTextField() == null) {
            ShadeVisitBehavior shadeVisitBehavior = new ShadeVisitBehavior();
            shadeVisitBehavior.setExcepted(textField);
            shadeVisitBehavior.setShadeAction(ShadeVisitBehavior.ShadeAction.Show);
            contentTraverser.setBehavior(shadeVisitBehavior);
            contentTraverser.traverse(window.getLayout(), false);
            if (window.getScroller() != null) {
                window.getScroller().setVisible(true);
            }
        }
    }
}
