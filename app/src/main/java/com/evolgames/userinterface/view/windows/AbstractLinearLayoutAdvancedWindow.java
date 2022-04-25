package com.evolgames.userinterface.view.windows;


import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Point;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.basics.Scroller;

public abstract class AbstractLinearLayoutAdvancedWindow<L extends LinearLayout> extends AdvancedWindow<LinearLayoutAdvancedWindowController<?>> {
    protected L layout;
    private Scroller scroller;
    private Point visibilitySup;
    private Point visibilityInf;
    public AbstractLinearLayoutAdvancedWindow(float pX, float pY, int rows, int columns, LinearLayoutAdvancedWindowController<?> controller) {
        this(pX, pY, rows, columns, true,controller);
    }
    public AbstractLinearLayoutAdvancedWindow(float pX, float pY, int rows, int columns,boolean hasPadding, LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, controller,hasPadding);
        visibilitySup = new Point(0f,rows * TILE_SIDE-rows - 5f);
        visibilityInf = new Point(0f,5f);
        addElement(visibilitySup);
        addElement(visibilityInf);
        layout = createLayout();
        layout.setDepth(2);
        layout.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        addElement(layout);
    }

    public Scroller getScroller() {
        return scroller;
    }

    public void setScroller(Scroller scroller) {
        this.scroller = scroller;
    }

    public float getVisibilitySup() {
        return visibilitySup.getAbsoluteY();
    }

    public float getVisibilityInf() {
        return visibilityInf.getAbsoluteY();
    }

    public float getLocalVisibilitySup() {
        return visibilitySup.getLowerBottomY();
    }

    public L getLayout() {
        return layout;
    }

    protected abstract L createLayout();

public float getVisibilityZoneLength(){
    return getVisibilitySup() - getVisibilityInf();

}
    public void createScroller(){
    if(mRows<3)throw new UnsupportedOperationException();
        Scroller scroller = new Scroller(TILE_SIDE*mColumns-Scroller.UPPER_WIDTH/2,getLocalVisibilitySup(), mRows-2,getController(),getVisibilityZoneLength());
       // setWidth(getWidth()+Scroller.UPPER_WIDTH/2);
        setScroller(scroller);
        scroller.setDepth(5);
        addElement(scroller);
        scroller.onHeightUpdated(getLayout().getHeight());
    }


}
